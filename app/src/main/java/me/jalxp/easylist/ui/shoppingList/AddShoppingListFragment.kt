package me.jalxp.easylist.ui.shoppingList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import me.jalxp.easylist.R
import me.jalxp.easylist.data.entities.ShoppingList
import me.jalxp.easylist.databinding.FragmentAddShoppingListBinding
import me.jalxp.easylist.viewmodels.ProductsViewModel
import me.jalxp.easylist.viewmodels.ProductsViewModelFactory
import me.jalxp.easylist.viewmodels.ShoppingListViewModelFactory
import me.jalxp.easylist.viewmodels.ShoppingViewModel

class AddShoppingListFragment : Fragment() {

    private lateinit var binding: FragmentAddShoppingListBinding
    private val shoppingViewModel: ShoppingViewModel by activityViewModels {
        ShoppingListViewModelFactory(requireContext())
    }
    private val productsViewModel: ProductsViewModel by activityViewModels {
        ProductsViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddShoppingListBinding.inflate(inflater, container, false)

        /* Populate dropdown list*/
        val shoppingLists: List<ShoppingList> = shoppingViewModel.getAllShoppingListsNonLive()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, shoppingLists)
        binding.copyListAutoComplete.setAdapter(adapter)


        binding.createListButton.setOnClickListener {
            createShoppingList()
        }

        return binding.root
    }

    private fun createShoppingList() {
        val listTitle = binding.createShoppingListTitle.text.toString()
        val copyFromTitle = binding.copyListAutoComplete.text.toString()

        when {
            listTitle.isEmpty() -> {
                binding.shoppingListInputLayout.error = getString(R.string.need_name_error)
                return
            }
            shoppingViewModel.shoppingListAlreadyExists(listTitle) -> {
                binding.shoppingListInputLayout.error = getString(R.string.shopping_list_already_exists)
                return
            }
            else -> {
                if (copyFromTitle == getString(R.string.default_none_value)) {
                    shoppingViewModel.insertNewShoppingList(listTitle)
                } else {
                    val copyFromShoppingId = shoppingViewModel.getShoppingListIdFromName(copyFromTitle)
                    Log.e("<AddShoppingFrag>", "copyFromId: $copyFromShoppingId")
                    if (copyFromShoppingId != null)
                        createShoppingListFromCopy(listTitle, copyFromShoppingId)
                }
                findNavController().navigate(R.id.action_addShoppingListFragment_to_nav_shoppinglists)
            }
        }
    }

    private fun createShoppingListFromCopy(listTitle: String, baseListId: Long) {
        shoppingViewModel.insertNewShoppingList(listTitle)
        val newShoppingListId = shoppingViewModel.getShoppingListIdFromName(listTitle)
        productsViewModel.getProductsByShoppingListIdNonLive(baseListId).forEach {
            Log.e("<AddShoppingFrag>", "it: ${it.name}")
            productsViewModel.insertNewProduct(
                it.name,
                it.description,
                it.quantity ?: 1,
                it.measureUnitId,
                it.categoryId,
                newShoppingListId!!,
                it.brand,
                it.barCode,
                it.imagePath
            )
        }

    }
}