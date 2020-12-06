package me.jalxp.easylist.ui.products

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import me.jalxp.easylist.R
import me.jalxp.easylist.data.entities.Product
import me.jalxp.easylist.databinding.FragmentProductDetailBinding
import me.jalxp.easylist.viewmodels.CategoriesViewModel
import me.jalxp.easylist.viewmodels.CategoriesViewModelFactory
import me.jalxp.easylist.viewmodels.MeasurementUnitsViewModel
import me.jalxp.easylist.viewmodels.MeasurementUnitsViewModelFactory
import me.jalxp.easylist.ui.shoppingList.EXTRA_PRODUCT_ID
import me.jalxp.easylist.viewmodels.ShoppingListViewModelFactory
import me.jalxp.easylist.viewmodels.ShoppingViewModel
import me.jalxp.easylist.viewmodels.PricesViewModel
import me.jalxp.easylist.viewmodels.PricesViewModelFactory
import me.jalxp.easylist.viewmodels.ProductsViewModel
import me.jalxp.easylist.viewmodels.ProductsViewModelFactory

class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding
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
    private val pricesViewModel: PricesViewModel by activityViewModels {
        PricesViewModelFactory(requireContext())
    }

    private lateinit var product: Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailBinding.inflate(layoutInflater, container, false)

        val productId = arguments?.getLong(EXTRA_PRODUCT_ID)
        product = productsViewModel.getProductById(productId!!)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {

        with(binding) {
            if (product.imagePath != null) {
                productImageView.setImageBitmap(BitmapFactory.decodeFile(product.imagePath))
            }

            var shoppingListName = getString(R.string.default_none_value)
            if (product.shoppingListId != null)
                shoppingListName =
                    shoppingViewModel.getShoppingListById(product.shoppingListId!!).title
            shoppingListTextInputEditText.setText(shoppingListName)

            nameTextInputEditText.setText(product.name)
            descriptionTextInputEditText.setText(product.description)
            quantityTextInputEditText.setText(product.quantity.toString())

            var productMeasurementUnit = ""
            if (product.measureUnitId != null)
                productMeasurementUnit =
                    measureUnitsViewModel.getMeasurementUnitById(product.measureUnitId!!).designation
            measurementUnitAutoCompleteTextView.setText(productMeasurementUnit)

            var productCategory = ""
            if (product.categoryId != null)
                productCategory =
                    categoriesViewModel.getCategoryById(product.categoryId!!).designation
            categoryAutoCompleteTextView.setText(productCategory)

            brandTextInputEditText.setText(product.brand)

            val lowestPrice =
                getString(R.string.money_symbol) + pricesViewModel.getMinPriceByProductId(product.productId)
                    .toString()
            val highestPrice =
                getString(R.string.money_symbol) + pricesViewModel.getMaxPriceByProductId(product.productId)
                    .toString()
            val prices = pricesViewModel.getAllPricesByProductId(product.productId)

            lowestPriceTextInputEditText.setText(lowestPrice)
            highestPriceTextInputEditText.setText(highestPrice)
            var pricesStr = ""
            prices.forEach {
                pricesStr += getString(R.string.money_symbol) + it.toString() + "\n"
            }
            pricesTextInputEditText.setText(pricesStr)
        }

        // TODO prices, save button, etc
    }
}