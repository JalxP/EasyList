package me.jalxp.easylist.ui.products

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import me.jalxp.easylist.adapters.ExpandableCategoriesAdapter
import me.jalxp.easylist.data.entities.Category
import me.jalxp.easylist.data.entities.Product
import me.jalxp.easylist.databinding.FragmentCategorySortBinding
import me.jalxp.easylist.ui.categories.CategoriesViewModel
import me.jalxp.easylist.ui.categories.CategoriesViewModelFactory
import me.jalxp.easylist.ui.measurementUnits.MeasurementUnitsViewModel
import me.jalxp.easylist.ui.measurementUnits.MeasurementUnitsViewModelFactory
import me.jalxp.easylist.ui.shoppingList.EXTRA_LIST_ID
import me.jalxp.easylist.ui.shoppingList.ShoppingListViewModelFactory
import me.jalxp.easylist.ui.shoppingList.ShoppingViewModel


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
            productsByCategory
        )

        binding.expandableListView.setAdapter(adapter)
        binding.expandableListView.setOnGroupExpandListener { groupPosition ->
            Toast.makeText(
                requireContext(),
                "${categoriesList[groupPosition]} List Expanded.",
                Toast.LENGTH_LONG
            ).show()
        }


        return binding.root
    }
}