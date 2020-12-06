package me.jalxp.easylist.ui.shoppingList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import me.jalxp.easylist.R
import me.jalxp.easylist.adapters.ShoppingListsAdapter
import me.jalxp.easylist.databinding.FragmentShoppingListsBinding
import me.jalxp.easylist.data.entities.ShoppingList
import me.jalxp.easylist.viewmodels.ProductsViewModel
import me.jalxp.easylist.viewmodels.ProductsViewModelFactory
import me.jalxp.easylist.viewmodels.ShoppingListViewModelFactory
import me.jalxp.easylist.viewmodels.ShoppingViewModel

const val EXTRA_LIST_ID = "extra list id"
const val EXTRA_LIST_TITLE = "extra list name"
const val EXTRA_PRODUCT_ID = "extra product id"
const val EXTRA_PRODUCT_NAME = "extra product name"

class ShoppingListsFragment : Fragment() {

    private lateinit var binding: FragmentShoppingListsBinding
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
        // View Binding
        binding = FragmentShoppingListsBinding.inflate(inflater, container, false)

        /* RecyclerView and Listeners */
        val shoppingListsAdapter = ShoppingListsAdapter(
            { shoppingList -> adapterOnItemClick(shoppingList) },
            { shoppingList -> adapterOnEditClick(shoppingList) },
            { shoppingList -> adapterOnDeleteClick(shoppingList) }
        )

        with(binding) {
            recycleViewList.adapter = shoppingListsAdapter
            recycleViewList.layoutManager =
                LinearLayoutManager(this@ShoppingListsFragment.context, LinearLayoutManager.VERTICAL, false)
        }

        shoppingViewModel.shoppingListsLiveData.observe(viewLifecycleOwner, {
            it?.let {
                shoppingListsAdapter.submitList(it as MutableList<ShoppingList>)
            }
        })

        /* Float Action Button */
        binding.createListFab.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_nav_shoppinglists_to_addShoppingListFragment)
        }

        return binding.root
    }

    private fun adapterOnItemClick(shoppingList: ShoppingList) {
        val bundle = Bundle()
        bundle.putLong(EXTRA_LIST_ID, shoppingList.shoppingListId)
        bundle.putString(EXTRA_LIST_TITLE, shoppingList.title)

        findNavController().navigate(R.id.action_nav_shoppinglists_to_singleListFragment, bundle)
    }

    private fun adapterOnEditClick(shoppingList: ShoppingList) {
        // TODO allow shoppingList edit here.
        Snackbar.make(binding.root, "Not Yet Implemented", Snackbar.LENGTH_LONG).show()
    }

    private fun adapterOnDeleteClick(shoppingList: ShoppingList) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.delete_shopping_list_question))
            .setMessage(resources.getString(R.string.delete_shopping_list_message))
            .setNeutralButton(resources.getString(R.string.dialogue_cancel)) { dialog, which ->
                Snackbar.make(binding.root, "Canceled", Snackbar.LENGTH_LONG).show() // TODO strings
            }
            .setPositiveButton(resources.getString(R.string.delete_shopping_list_confirm)) { dialog, which ->
                Snackbar.make(binding.root, "List Deleted", Snackbar.LENGTH_LONG).show() // TODO strings
                shoppingViewModel.removeShoppingList(shoppingList)
                productsViewModel.clearAllAssociationsWithShoppingList(shoppingList.shoppingListId)
            }
            .show()
    }
}