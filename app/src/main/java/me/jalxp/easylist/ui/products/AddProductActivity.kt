package me.jalxp.easylist.ui.products

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import me.jalxp.easylist.R
import me.jalxp.easylist.databinding.ActivityAddProductBinding
import me.jalxp.easylist.ui.categories.CategoriesViewModel
import me.jalxp.easylist.ui.categories.CategoriesViewModelFactory
import me.jalxp.easylist.ui.measurementUnits.MeasurementUnitsViewModel
import me.jalxp.easylist.ui.measurementUnits.MeasurementUnitsViewModelFactory

const val PRODUCT_NAME = "product name"
const val PRODUCT_DESCRIPTION = "product description"
const val PRODUCT_QUANTITY = "product quantity"
const val PRODUCT_MEASUREMENT_UNIT = "product measurement unit"
const val PRODUCT_CATEGORY = "product category"
const val PRODUCT_BRAND = "product brand"

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private val categoriesViewModel: CategoriesViewModel by viewModels {
        CategoriesViewModelFactory(this)
    }
    private val measurementUnitsViewModel: MeasurementUnitsViewModel by viewModels {
        MeasurementUnitsViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        title = getString(R.string.activity_add_product)
        setSupportActionBar(binding.addProductListToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Populate dropdown lists
        val categoriesArray = mutableListOf<String>()
        categoriesViewModel.categoriesLiveData.value?.forEach {
            categoriesArray.add(it.designation)
        }
        val categoriesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoriesArray)
        (binding.categoryAutoComplete as? AutoCompleteTextView)?.setAdapter(categoriesAdapter)

        val measurementsArray = mutableListOf<String>()
        measurementUnitsViewModel.measurementUnitsLiveData.value?.forEach {
            measurementsArray.add(it.designation)
        }
        val measurementUnitsAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, measurementsArray)
        (binding.measurementUnitAutoComplete as? AutoCompleteTextView)?.setAdapter(
            measurementUnitsAdapter
        )

        /* Float Action Button */
        binding.addProductButton.setOnClickListener {
            addProduct()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun addProduct() {
        val resultIntent = Intent()

        val productName = binding.productNameInput.text.toString()
        if (productName.isNullOrEmpty()) {
            binding.productNameTextLayout.error = getString(R.string.need_name_error)
            return
        }

        val productDescription = binding.productDescriptionInput.text.toString()
        val productQuantityStr = binding.productQuantityInput.text.toString()
        var productQuantity = 1
        if (productQuantityStr.isNotEmpty())
            productQuantity = productQuantityStr.toInt()

        val productMeasureUnit = binding.measurementUnitAutoComplete.text.toString()
        val productCategory = binding.categoryAutoComplete.text.toString()

        val productBrand = binding.productBrandInput.text.toString()

        val valid = checkValidity(productName, productDescription, productQuantity, productMeasureUnit, productCategory, productBrand)
        if (!valid) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
        } else {
            resultIntent.putExtra(PRODUCT_NAME, productName)
            resultIntent.putExtra(PRODUCT_DESCRIPTION, productDescription)
            resultIntent.putExtra(PRODUCT_QUANTITY, productQuantity)
            resultIntent.putExtra(PRODUCT_MEASUREMENT_UNIT, productMeasureUnit)
            resultIntent.putExtra(PRODUCT_CATEGORY, productCategory)
            resultIntent.putExtra(PRODUCT_BRAND, productBrand)
            setResult(Activity.RESULT_OK, resultIntent)
        }

        finish()
    }

    private fun checkValidity(
        productName: String,
        productDescription: String,
        productQuantity: Int,
        productMeasureUnit: String,
        productCategory: String,
        productBrand: String
    ): Boolean {
        // TODO
        return true
    }
}