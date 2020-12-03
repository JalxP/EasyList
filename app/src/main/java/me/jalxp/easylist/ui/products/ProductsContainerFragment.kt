package me.jalxp.easylist.ui.products

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import me.jalxp.easylist.R
import me.jalxp.easylist.adapters.ProductsListAdapter
import me.jalxp.easylist.data.entities.Product
import me.jalxp.easylist.databinding.FragmentContainerProductsBinding
import me.jalxp.easylist.ui.categories.CategoriesViewModel
import me.jalxp.easylist.ui.categories.CategoriesViewModelFactory
import me.jalxp.easylist.ui.measurementUnits.MeasurementUnitsViewModel
import me.jalxp.easylist.ui.measurementUnits.MeasurementUnitsViewModelFactory

import me.jalxp.easylist.ui.shoppingList.EXTRA_LIST_ID
import me.jalxp.easylist.ui.shoppingList.EXTRA_PRODUCT_ID

const val VIEW_TYPE = "view type"
const val SHOW_PRODUCTS_ALL = 1
const val SHOW_PRODUCTS_BY_LIST = 2
const val SHOW_PRODUCTS_BY_CATEGORY = 3

class ProductsContainerFragment : Fragment() {

    private lateinit var binding: FragmentContainerProductsBinding
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
        binding = FragmentContainerProductsBinding.inflate(layoutInflater, container, false)
        if (savedInstanceState != null)
            return binding.root

        /* RecycleView */
        val productsAdapter = ProductsListAdapter(
            categoriesViewModel,
            measureUnitsViewModel,
            { product -> adapterOnItemClick(product) },
            { product -> adapterOnItemLongClick(product) }
        )

        with(binding) {
            recycleViewList.adapter = productsAdapter
            val gridColumnCount =
                resources.getInteger(R.integer.grid_column_count)
            recycleViewList.layoutManager =
                GridLayoutManager(
                    context,
                    gridColumnCount,
                    GridLayoutManager.VERTICAL,
                    false
                )
        }

        when (requireArguments().getInt(VIEW_TYPE)) {
            SHOW_PRODUCTS_ALL -> {
                productsViewModel.getAllProducts().observe(viewLifecycleOwner, {
                    productsAdapter.submitList(it)
                })
            }
            SHOW_PRODUCTS_BY_LIST -> {
                shoppingListId = requireArguments().getLong(EXTRA_LIST_ID)
                productsViewModel.getProductsByShoppingListId(shoppingListId!!)
                    .observe(viewLifecycleOwner, {
                        productsAdapter.submitList(it)
                    })
            }
            SHOW_PRODUCTS_BY_CATEGORY -> {

            }
        }

        return binding.root
    }

    private fun adapterOnItemClick(product: Product) {

        val bundle = Bundle()
        bundle.putLong(EXTRA_PRODUCT_ID, product.productId)

        // Because we have 2 origins with the same destination
        var destination = R.id.action_nav_products_to_productDetailFragment
        if (findNavController().currentDestination?.id == R.id.nav_singleListFragment) {
            destination = R.id.action_singleListFragment_to_productDetailFragment
        }

        findNavController().navigate(destination, bundle)
    }

    private fun adapterOnItemLongClick(product: Product) {
        if (findNavController().currentDestination?.id == R.id.nav_singleListFragment) {
            if (!product.onCart) {
                product.onCart = true
                productsViewModel.updateProduct(product)
                binding.recycleViewList.adapter?.notifyDataSetChanged()
                // TODO wtf is going on here
                val str =
                    getString(R.string.prefix_item_added_to_cart) + product.name + getString(R.string.posfix_item_added_to_cart)
                Snackbar.make(binding.root, str, Snackbar.LENGTH_SHORT).show()
            } else {
                val str = product.name + getString(R.string.posfix_item_already_on_cart)
                Snackbar.make(binding.root, str, Snackbar.LENGTH_SHORT).show()
            }
        } else {
            Log.e("<DEBUG>", "Do not add ${product.name} to shopping cart!")
        }
    }
}