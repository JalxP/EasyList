package me.jalxp.easylist.ui.products

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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

const val DECREASE = -1
const val INCREASE = 1

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
    private var changed: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailBinding.inflate(layoutInflater, container, false)

        val productId = arguments?.getLong(EXTRA_PRODUCT_ID)
        product = productsViewModel.getProductById(productId!!)

        /* Button Listeners */
        binding.decreaseImageButton.setOnClickListener {
            changeQuantity(DECREASE)
        }
        binding.increaseImageButton.setOnClickListener {
            changeQuantity(INCREASE)
        }
        binding.saveProductButton.setOnClickListener {
            saveProduct()
        }

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

            /* Populate dropdown lists */
            categoriesViewModel.categoriesLiveData.observe(
                viewLifecycleOwner,
                { dropdownData ->
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        dropdownData
                    )
                    (binding.categoryAutoCompleteTextView as? AutoCompleteTextView)?.setAdapter(
                        adapter
                    )
                })

            measureUnitsViewModel.measurementUnitsLiveData.observe(
                viewLifecycleOwner,
                Observer { dropdownData ->
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        dropdownData
                    )
                    (binding.measurementUnitAutoCompleteTextView as? AutoCompleteTextView)?.setAdapter(
                        adapter
                    )
                })

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
    }

    private fun changeQuantity(type: Int) {
        val current = binding.quantityTextInputEditText.text.toString().toInt() + type
        if (current < 1) {
            Snackbar.make(
                binding.root,
                getString(R.string.invalid_quantity_message),
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }
        changed = true
        product.quantity = current
        productsViewModel.updateProduct(product)
        binding.quantityTextInputEditText.setText(current.toString())
    }

    private fun saveProduct() {
        val productName = binding.nameTextInputEditText.text.toString()
        val productDescription = binding.descriptionTextInputEditText.text.toString()
        val productUnit = binding.measurementUnitAutoCompleteTextView.text.toString()
        val productCategory = binding.categoryAutoCompleteTextView.text.toString()
        val productBrand = binding.brandTextInputEditText.text.toString()

        if (productName != product.name) {
            if (productName.isEmpty()) {
                binding.nameTextInputLayout.error = getString(R.string.need_name_error)
                return
            }
            if (product.shoppingListId != null) {
                if (productsViewModel.productAlreadyExistsInTheShoppingList(
                        productName, productBrand,
                        product.shoppingListId!!
                    )
                ) {
                    binding.nameTextInputLayout.error =
                        getString(R.string.product_already_exists_on_list)
                    return
                }
            } else {
                if (productsViewModel.productWithSameNameAlreadyExists(productName, productBrand)) {
                    binding.nameTextInputLayout.error =
                        getString(R.string.product_with_the_same_name_already_exists)
                    return
                }
            }
            product.name = productName
        }

        if (productDescription != product.description) {
            product.description = productDescription
            changed = true
        }

        if (productUnit.isNotEmpty()) {
            val measureUnitId =
                measureUnitsViewModel.getMeasurementUnitByDesignation(productUnit).measureUnitId
            if (measureUnitId != product.measureUnitId) {
                product.measureUnitId = measureUnitId
                changed = true
            }
        }

        if (productCategory.isNotEmpty()) {
            val categoryId =
                categoriesViewModel.getCategoryByDesignation(productCategory).categoryId
            if (categoryId != product.categoryId) {
                product.categoryId = categoryId
                changed = true
            }
        }

        if (productBrand != product.brand) {
            if (product.shoppingListId != null) {
                if (productsViewModel.productAlreadyExistsInTheShoppingList(
                        productName, productBrand,
                        product.shoppingListId!!
                    )
                ) {
                    binding.nameTextInputLayout.error =
                        getString(R.string.product_already_exists_on_list)
                    return
                }
            } else {
                if (productsViewModel.productWithSameNameAlreadyExists(productName, productBrand)) {
                    binding.nameTextInputLayout.error =
                        getString(R.string.product_with_the_same_name_already_exists)
                    return
                }
            }
            product.brand = productBrand
            changed = true
        }

        productsViewModel.updateProduct(product)

        if (changed) {
            Snackbar.make(
                binding.root,
                getString(R.string.prefix_product_changes_saved) + product.name + getString(R.string.posfix_product_changes_saved),
                Snackbar.LENGTH_SHORT
            ).show()
        }
        navigate()
    }

    private fun navigate() {
        findNavController().navigateUp()
    }
}