package me.jalxp.easylist.ui.shoppingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import me.jalxp.easylist.R
import me.jalxp.easylist.databinding.FragmentAddShoppingListBinding

class AddShoppingListFragment : Fragment() {

    private lateinit var binding: FragmentAddShoppingListBinding
    private val shoppingViewModel: ShoppingViewModel by activityViewModels {
        ShoppingListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddShoppingListBinding.inflate(inflater, container, false)

        binding.createListButton.setOnClickListener {
            createShoppingList()
        }

        return binding.root
    }

    private fun createShoppingList() {
        val listTitle = binding.createShoppingListTitle.text.toString()

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
                shoppingViewModel.insertNewShoppingList(listTitle)
                findNavController().navigate(R.id.action_addShoppingListFragment_to_nav_shoppinglists)
            }
        }
    }
}