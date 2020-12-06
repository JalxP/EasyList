package me.jalxp.easylist.ui.products

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import me.jalxp.easylist.R
import me.jalxp.easylist.adapters.ExpandableCategoriesAdapter
import me.jalxp.easylist.data.entities.Category
import me.jalxp.easylist.data.entities.Product
import me.jalxp.easylist.databinding.FragmentCategorySortBinding
import me.jalxp.easylist.viewmodels.CategoriesViewModel
import me.jalxp.easylist.viewmodels.CategoriesViewModelFactory
import me.jalxp.easylist.viewmodels.MeasurementUnitsViewModel
import me.jalxp.easylist.viewmodels.MeasurementUnitsViewModelFactory
import me.jalxp.easylist.ui.shoppingList.EXTRA_LIST_ID
import me.jalxp.easylist.viewmodels.ShoppingListViewModelFactory
import me.jalxp.easylist.viewmodels.ShoppingViewModel
import me.jalxp.easylist.viewmodels.ProductsViewModel
import me.jalxp.easylist.viewmodels.ProductsViewModelFactory


class CategorySortFragment : Fragment() {

    private lateinit var binding: FragmentCategorySortBinding

    private val productsViewModel: ProductsViewModel by activityViewModels {
        ProductsViewModelFactory(requireContext())
    }
    private val measureUnitsViewModel: MeasurementUnitsViewModel by activityViewModels {
        MeasurementUnitsViewModelFactory(requireContext())
    }
    private val categoriesViewModel: CategoriesViewModel by activityViewModels {
        CategoriesViewModelFactory(requireContext())
    }

    private val shoppingViewModel: ShoppingViewModel by activityViewModels {
        ShoppingListViewModelFactory(requireContext())
    }

    private var shoppingListId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            shoppingListId = it.getLong(EXTRA_LIST_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCategorySortBinding.inflate(inflater, container, false)

        // Obter a lista de compras
        val categories = categoriesViewModel.getAllCategoriesNonLive()
        val productsOnList = productsViewModel.getProductsByShoppingListIdNonLive(shoppingListId!!)
        val categoriesOnShopingList = mutableSetOf<Category>()

        productsOnList.forEach { p ->
            categories.forEach { c ->
                if (p.categoryId == c.categoryId)
                    categoriesOnShopingList.add(c)
            }
        }
        if (categoriesOnShopingList.isEmpty())
            return binding.root


        val productsByCategory = mutableMapOf<Category, MutableList<Product>>()
        categoriesOnShopingList.forEach { category ->
            productsOnList.forEach { product ->
                if (product.categoryId == category.categoryId) {
                    if (!productsByCategory.containsKey(category)) {
                        productsByCategory[category] = mutableListOf(product)
                    } else {
                        productsByCategory[category]?.add(product)
                    }
                }
            }
        }

        val categoriesList = categoriesOnShopingList.toList()
        val adapter = ExpandableCategoriesAdapter(
            requireContext(),
            categoriesList,
            productsByCategory,
            { product -> adapterOnItemClick(product) },
            { product -> adapterOnItemLongClick(product) }
        )

        with(binding) {
            expandableListView.setAdapter(adapter)
        }

        return binding.root
    }

    private fun adapterOnItemClick(product: Product) {
        Log.e("<CLICK>", product.name)

    }

    private fun adapterOnItemLongClick(product: Product) {
        when (findNavController().currentDestination?.id) {
            R.id.nav_singleListFragment -> {
                product.onCart = !product.onCart
                productsViewModel.updateProduct(product)
                (binding.expandableListView.expandableListAdapter as? ExpandableCategoriesAdapter)?.notifyDataSetChanged()

                Snackbar.make(
                    binding.root,
                    if (product.onCart)
                        getString(R.string.prefix_item_added_to_cart) + product.name + getString(R.string.posfix_item_added_to_cart)
                    else
                        getString(R.string.prefix_item_removed_from_cart) + product.name + getString(
                            R.string.posfix_item_removed_from_cart
                        ), Snackbar.LENGTH_SHORT
                ).show()

            }
            else -> {
                Log.e("<DEBUG>", "Do not add ${product.name} to shopping cart!")
            }
        }
    }
}