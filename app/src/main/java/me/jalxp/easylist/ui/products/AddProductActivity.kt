package me.jalxp.easylist.ui.products

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import me.jalxp.easylist.R
import me.jalxp.easylist.data.entities.Category
import me.jalxp.easylist.databinding.ActivityAddProductBinding
import me.jalxp.easylist.ui.categories.CategoriesViewModel
import me.jalxp.easylist.ui.categories.CategoriesViewModelFactory

const val PRODUCT_NAME = "product name"
const val PRODUCT_DESCRIPTION = "product description"
const val PRODUCT_QUANTITY = "product quantity"
const val PRODUCT_MEASUREMENT_UNIT = "product measurement unit"
const val PRODUCT_CATEGORY = "product category"

class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private val categoriesViewModel: CategoriesViewModel by viewModels<CategoriesViewModel> {
        CategoriesViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        title = getString(R.string.activity_add_product)
        setSupportActionBar(binding.addProductListToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // TODO CHECK THIS

        // Populate spinners here
        val possibleCategories = categoriesViewModel.getAllCategories().value
        val tempList = mutableListOf<Category>()
        possibleCategories?.forEach {
            tempList.add(it)
        }

        val categorySpinnerAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, tempList)
        binding.categoryPickerSpinner.adapter = categorySpinnerAdapter

        // TODO other spinner here

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
        val productDescription = binding.productDescriptionInput.text.toString()
        var productQuantity = 0
        if (!binding.productQuantityInput.text.toString().isNullOrEmpty()) {
            productQuantity = binding.productQuantityInput.text.toString().toInt()
        }
        val productCategory = binding.categoryPickerSpinner.selectedItem?.toString()

        val valid = checkValidity()
        if (!valid) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
        } else {
            resultIntent.putExtra(PRODUCT_NAME, productName)
            resultIntent.putExtra(PRODUCT_DESCRIPTION, productDescription)
            resultIntent.putExtra(PRODUCT_QUANTITY, productQuantity)
            resultIntent.putExtra(PRODUCT_CATEGORY, productCategory)
            setResult(Activity.RESULT_OK, resultIntent)
        }

        finish()
    }

    private fun checkValidity() : Boolean {
        // TODO
        return true
    }
}