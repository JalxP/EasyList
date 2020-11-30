package me.jalxp.easylist.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import me.jalxp.easylist.R
import me.jalxp.easylist.databinding.FragmentAddProductBinding
import me.jalxp.easylist.ui.categories.CategoriesViewModel
import me.jalxp.easylist.ui.categories.CategoriesViewModelFactory
import me.jalxp.easylist.ui.measurementUnits.MeasurementUnitsViewModel
import me.jalxp.easylist.ui.measurementUnits.MeasurementUnitsViewModelFactory
import me.jalxp.easylist.ui.shoppingList.EXTRA_LIST_ID

class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private var shoppingListId: Long? = null

    private val productsViewModel: ProductsViewModel by activityViewModels {
        ProductsViewModelFactory(requireContext())
    }

    private val categoriesViewModel: CategoriesViewModel by activityViewModels {
        CategoriesViewModelFactory(requireContext())
    }
    private val measurementUnitsViewModel: MeasurementUnitsViewModel by activityViewModels {
        MeasurementUnitsViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)

        shoppingListId = requireArguments().getLong(EXTRA_LIST_ID)

        /* Populate dropdown lists */
        categoriesViewModel.categoriesLiveData.observe(
            viewLifecycleOwner,
            Observer { dropdownData ->
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    dropdownData
                )
                (binding.categoryAutoComplete as? AutoCompleteTextView)?.setAdapter(adapter)
            })

        measurementUnitsViewModel.measurementUnitsLiveData.observe(
            viewLifecycleOwner,
            Observer { dropdownData ->
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    dropdownData
                )
                (binding.measurementUnitAutoComplete as? AutoCompleteTextView)?.setAdapter(adapter)
            })

        /* Imageview select image / take photo */
        // TODO

        /* Float Action Button */
        binding.addProductButton.setOnClickListener {
            addProductClicked()
        }

        return binding.root
    }

    private fun addProductClicked() {
        val productName = binding.productNameInput.text.toString()
        if (productName.isEmpty()) {
            binding.productNameTextLayout.error = getString(R.string.need_name_error)
            return
        } else if (productsViewModel.productAlreadyExistsInTheShoppingList(
                productName,
                shoppingListId!!
            )
        ) {
            binding.productNameTextLayout.error = getString(R.string.product_already_exists_on_list)
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

        addNewProduct(
            productName,
            productDescription,
            productQuantity,
            productMeasureUnit,
            productCategory,
            productBrand
        )
        findNavController().navigate(
            R.id.action_addProductFragment_to_singleListFragment,
            arguments
        )
    }

    private fun addNewProduct(
        productName: String,
        productDescription: String,
        productQuantity: Int,
        productMeasureUnit: String,
        productCategory: String,
        productBrand: String
    ) {

        var productMeasureUnitId: Long? = null
        if (productMeasureUnit.isNotEmpty())
            productMeasureUnitId =
                measurementUnitsViewModel.getMeasurementUnitByDesignation(productMeasureUnit).value?.measureUnitId

        var productCategoryId: Long? = null
        if (productCategory.isNotEmpty())
            productCategoryId =
                categoriesViewModel.getCategoryByDesignation(productCategory).value?.categoryId

        productsViewModel.insertNewProduct(
            productName,
            productDescription,
            productQuantity,
            productMeasureUnitId,
            productCategoryId,
            shoppingListId!!,
            productBrand,
            null, // TODO
            null // TODO
        )
    }
}