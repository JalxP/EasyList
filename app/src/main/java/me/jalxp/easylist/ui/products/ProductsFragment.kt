package me.jalxp.easylist.ui.products

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import me.jalxp.easylist.adapters.ProductsListAdapter
import me.jalxp.easylist.data.entities.Product
import me.jalxp.easylist.data.entities.ShoppingList
import me.jalxp.easylist.databinding.FragmentProductsBinding
import me.jalxp.easylist.ui.categories.CategoriesViewModel
import me.jalxp.easylist.ui.categories.CategoriesViewModelFactory
import me.jalxp.easylist.ui.measurementUnits.MeasurementUnitsViewModel
import me.jalxp.easylist.ui.measurementUnits.MeasurementUnitsViewModelFactory
import me.jalxp.easylist.ui.shoppingList.EXTRA_LIST_ID
import me.jalxp.easylist.ui.shoppingList.EXTRA_LIST_TITLE
import me.jalxp.easylist.ui.shoppingList.SingleListActivity

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

    private val addProductActivityRequestCode = 1
    private var shoppingListId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* View Binding */
        binding = FragmentProductsBinding.inflate(layoutInflater, container, false)

        shoppingListId = requireArguments().getLong(EXTRA_LIST_ID)

        /* RecycleView */
        val productsAdapter = ProductsListAdapter(
            categoriesViewModel,
            measureUnitsViewModel
        ) { product -> adapterOnItemClick(product) }

        with(binding) {
            recycleViewList.adapter = productsAdapter
            recycleViewList.layoutManager = GridLayoutManager(this@ProductsFragment.context, 3)
        }

        productsViewModel.productsLiveData.observe(viewLifecycleOwner, {
            it?.let {
                productsAdapter.submitList(it as MutableList<Product>)
            }
        })

        /* Floating Button */
        binding.addProductFab.setOnClickListener {
            addProductOnClick()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        Log.e("<Products Fragment>", "RequestCode: $requestCode resultCode == OK: ${resultCode == Activity.RESULT_OK}")

        if (requestCode == addProductActivityRequestCode && resultCode == Activity.RESULT_OK)
        {
            intentData?.let { data ->
                val productName = data.getStringExtra(PRODUCT_NAME) ?: ""
                val productDescription = data.getStringExtra(PRODUCT_DESCRIPTION) ?: ""
                val productQuantity = data.getIntExtra(PRODUCT_QUANTITY, 1)
                val productMeasureUnit = data.getStringExtra(PRODUCT_MEASUREMENT_UNIT) ?: ""
                val productCategory = data.getStringExtra(PRODUCT_CATEGORY) ?: ""
                val productBrand = data.getStringExtra(PRODUCT_BRAND) ?: ""

                addNewProduct(productName, productDescription, productQuantity, productMeasureUnit, productCategory, productBrand)
            }

        }
    }

    private fun addNewProduct(
        productName: String,
        productDescription: String,
        productQuantity: Int,
        productMeasureUnit: String,
        productCategory: String,
        productBrand: String
    ) {
        // TODO
        productsViewModel.insertNewProduct(productName, productDescription, productQuantity, null, null, shoppingListId!!, productBrand, null, null)
    }

    private fun adapterOnItemClick(product: Product) {
        // TODO
    }

    private fun addProductOnClick() {
        val intent = Intent(context, AddProductActivity::class.java)
        startActivityForResult(intent, addProductActivityRequestCode)
    }


}