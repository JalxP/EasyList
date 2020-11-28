package me.jalxp.easylist.ui.shoppingList

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import me.jalxp.easylist.adapters.ShoppingListsAdapter
import me.jalxp.easylist.databinding.FragmentShoppingListsBinding
import me.jalxp.easylist.data.entities.ShoppingList

const val EXTRA_LIST_ID = "extra list id"
const val EXTRA_LIST_TITLE = "extra list name"

class ShoppingListsFragment : Fragment() {

    private lateinit var binding: FragmentShoppingListsBinding
    private val newListActivityRequestCode = 1
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
        binding.createListFab.setOnClickListener {
            createListOnClick()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newListActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->

                val listTitle = data.getStringExtra(LIST_TITLE)
                if (listTitle != null)
                    shoppingViewModel.insertNewShoppingList(listTitle, null)
            }
        }
    }

    private fun adapterOnItemClick(shoppingList: ShoppingList) {
        val intent = Intent(context, SingleListActivity::class.java)
        Log.e(
            "<DEBUG>",
            "adapterOnItemClick: shoppingList: title=${shoppingList.title}, id=${shoppingList.shoppingListId}"
        )
        intent.putExtra(EXTRA_LIST_ID, shoppingList.shoppingListId)
        intent.putExtra(EXTRA_LIST_TITLE, shoppingList.title)
        startActivity(intent)
    }

    private fun adapterOnEditClick(shoppingList: ShoppingList) {
        // TODO allow shoppingList edit here.
        Toast.makeText(context, "adapterOnEditClick", Toast.LENGTH_LONG).show()
    }

    private fun adapterOnDeleteClick(shoppingList: ShoppingList) {
        // TODO pop up to confirm?
        shoppingViewModel.removeShoppingList(shoppingList)
    }

    private fun createListOnClick() {
        val intent = Intent(context, AddShoppingListActivity::class.java)
        startActivityForResult(intent, newListActivityRequestCode)
    }
}