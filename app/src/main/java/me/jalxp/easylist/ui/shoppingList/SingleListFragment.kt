package me.jalxp.easylist.ui.shoppingList

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import me.jalxp.easylist.R
import me.jalxp.easylist.databinding.FragmentSingleListBinding
import me.jalxp.easylist.ui.products.ProductsFragment

class SingleListFragment : Fragment(), OnItemSelectedListener {

    private lateinit var sortsArray: Array<String>
    private lateinit var binding: FragmentSingleListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSingleListBinding.inflate(inflater, container, false)

        val productsFragment = ProductsFragment()
        productsFragment.arguments = arguments
        findNavController().currentDestination?.label = arguments?.getString(EXTRA_LIST_TITLE)

        parentFragmentManager.beginTransaction().add(
            R.id.products_frame, productsFragment
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

        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(requireContext(), sortsArray[position], Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}