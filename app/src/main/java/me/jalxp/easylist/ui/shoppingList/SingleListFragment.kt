package me.jalxp.easylist.ui.shoppingList


import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import me.jalxp.easylist.R
import me.jalxp.easylist.databinding.FragmentSingleListBinding
import me.jalxp.easylist.ui.products.*

class SingleListFragment : Fragment(), OnItemSelectedListener {

    private lateinit var sortsArray: Array<String>
    lateinit var binding: FragmentSingleListBinding
    private val productsViewModel: ProductsViewModel by activityViewModels {
        ProductsViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSingleListBinding.inflate(inflater, container, false)

        val shoppingListId = requireArguments().getLong(EXTRA_LIST_ID)
        val shoppingListTitle = requireArguments().getString(EXTRA_LIST_TITLE)

        val productsContainerFragment = ProductsContainerFragment()
        val productFragmentArguments = Bundle().apply {
            putInt(VIEW_TYPE, SHOW_PRODUCTS_BY_LIST)
            putLong(EXTRA_LIST_ID, shoppingListId)
        }
        productsContainerFragment.arguments = productFragmentArguments


        parentFragmentManager.beginTransaction().add(
            R.id.products_frame, productsContainerFragment
        ).commit()

        /* Spinner */
        sortsArray = resources.getStringArray(R.array.sort_by_array)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item, sortsArray
        )

        with(binding) {
            sortBySpinner.adapter = adapter
            sortBySpinner.onItemSelectedListener = this@SingleListFragment
        }

        /* Floating Button */
        binding.addProductFab.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_singleListFragment_to_addProductFragment, arguments)
        }

        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(requireContext(), sortsArray[position], Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}