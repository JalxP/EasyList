package me.jalxp.easylist.ui.shoppingList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import me.jalxp.easylist.R
import me.jalxp.easylist.adapters.ShoppingListsAdapter
import me.jalxp.easylist.databinding.FragmentShoppingListsBinding
import me.jalxp.easylist.data.entities.ShoppingList
import me.jalxp.easylist.ui.products.ProductsFragment

const val EXTRA_LIST_ID = "extra list id"
const val EXTRA_LIST_TITLE = "extra list name"

class ShoppingListsFragment : Fragment() {

    private lateinit var binding: FragmentShoppingListsBinding
    private val shoppingViewModel: ShoppingViewModel by viewModels {
        ShoppingListViewModelFactory(requireContext())
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
        Toast.makeText(context, "adapterOnEditClick", Toast.LENGTH_LONG).show()
    }

    private fun adapterOnDeleteClick(shoppingList: ShoppingList) {
        // TODO pop up to confirm?
        shoppingViewModel.removeShoppingList(shoppingList)
    }
}