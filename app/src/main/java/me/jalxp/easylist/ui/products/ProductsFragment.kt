package me.jalxp.easylist.ui.products

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import me.jalxp.easylist.R
import me.jalxp.easylist.adapters.ProductsListAdapter
import me.jalxp.easylist.data.entities.Product
import me.jalxp.easylist.databinding.FragmentProductsBinding
import me.jalxp.easylist.ui.categories.CategoriesViewModel
import me.jalxp.easylist.ui.categories.CategoriesViewModelFactory
import me.jalxp.easylist.ui.measurementUnits.MeasurementUnitsViewModel
import me.jalxp.easylist.ui.measurementUnits.MeasurementUnitsViewModelFactory
import me.jalxp.easylist.ui.shoppingList.EXTRA_LIST_ID

class ProductsFragment : Fragment() {

    private lateinit var binding: FragmentProductsBinding
    private val productsViewModel: ProductsViewModel by activityViewModels {
        ProductsViewModelFactory(requireContext())
    }
    private val measureUnitsViewModel: MeasurementUnitsViewModel by activityViewModels {
        MeasurementUnitsViewModelFactory(requireContext())
    }
    private val categoriesViewModel: CategoriesViewModel by activityViewModels {
        CategoriesViewModelFactory(requireContext())
    }

    private var shoppingListId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /* View Binding */
        binding = FragmentProductsBinding.inflate(layoutInflater, container, false)
        if (savedInstanceState != null)
            return binding.root

        shoppingListId = requireArguments().getLong(EXTRA_LIST_ID)

        /* RecycleView */
        val productsAdapter = ProductsListAdapter(
            categoriesViewModel,
            measureUnitsViewModel
        ) { product -> adapterOnItemClick(product) }

        with(binding) {
            recycleViewList.adapter = productsAdapter
            val gridColumnCount =
                resources.getInteger(me.jalxp.easylist.R.integer.grid_column_count)
            recycleViewList.layoutManager =
                GridLayoutManager(this@ProductsFragment.context, gridColumnCount)
        }

        productsViewModel.getProductsByShoppingListId(shoppingListId!!)
            .observe(viewLifecycleOwner, {
                it?.let {
                    productsAdapter.submitList(it as MutableList<Product>)
                }
            })

        /* Floating Button */
        binding.addProductFab.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_singleListFragment_to_addProductFragment, arguments)
        }

        return binding.root
    }

    private fun adapterOnItemClick(product: Product) {
        // TODO
    }
}