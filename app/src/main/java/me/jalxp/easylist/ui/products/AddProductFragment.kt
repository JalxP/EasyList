package me.jalxp.easylist.ui.products

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import me.jalxp.easylist.ImageUtility
import me.jalxp.easylist.R
import me.jalxp.easylist.databinding.FragmentAddProductBinding
import me.jalxp.easylist.ui.categories.CategoriesViewModel
import me.jalxp.easylist.ui.categories.CategoriesViewModelFactory
import me.jalxp.easylist.ui.measurementUnits.MeasurementUnitsViewModel
import me.jalxp.easylist.ui.measurementUnits.MeasurementUnitsViewModelFactory
import me.jalxp.easylist.ui.shoppingList.EXTRA_LIST_ID
import java.io.File
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.*


const val GALLERY_REQUEST_CODE = 10
const val CAMERA_REQUEST_CODE = 20
const val GALLERY_PERMISSIONS_REQUEST_CODE = 100
const val CAMERA_PERMISSIONS_REQUEST_CODE = 200

class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private var shoppingListId: Long? = null
    private var filePath: String = ""

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

        shoppingListId = arguments?.getLong(EXTRA_LIST_ID)

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
        binding.importImageButton.setOnClickListener {
            selectImageFromGallery()
        }
        binding.captureImageButton.setOnClickListener {
            captureImage()
        }

        /* Float Action Button */
        binding.addProductButton.setOnClickListener {
            addProductClicked()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.apply {
                        val cursor = requireActivity().contentResolver.query(
                            this,
                            arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null
                        )
                        if (cursor != null && cursor.moveToFirst())
                            filePath = cursor.getString(0)
                        ImageUtility.setPic(
                            binding.productImageView,
                            filePath
                        )
                    }
                }
            }
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    ImageUtility.setPic(
                        binding.productImageView,
                        filePath
                    )
                }
            }
            else -> {
                // Ignorar outros requestCodes
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_PERMISSIONS_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    captureImage()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "The camera feature is unavailable.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
            GALLERY_PERMISSIONS_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    selectImageFromGallery()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "The media selection feature is unavailable.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return

            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun selectImageFromGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), GALLERY_PERMISSIONS_REQUEST_CODE
            )
        } else {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }
    }

    private fun captureImage() {

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                val m: Method = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
                ), CAMERA_PERMISSIONS_REQUEST_CODE
            )
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                val fileUri = Uri.fromFile(getOutputMediaFile())
                putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            }

            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
    }

    private fun getOutputMediaFile(): File? {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            return null
        }

        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "EasyList"
        )
        mediaStorageDir.apply {
            if (!exists()) {
                if (!mkdirs()) {
                    return null
                }
            }
        }

        // Create a media file name
        filePath = "${mediaStorageDir.path}${File.separator}${
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        }.jpg"
        return File(filePath)
    }

    private fun addProductClicked() {
        val productName = binding.productNameInput.text.toString()
        if (productName.isEmpty()) {
            binding.productNameTextLayout.error = getString(R.string.need_name_error)
            return
        }

        val productDescription = binding.productDescriptionInput.text.toString()
        val productQuantityStr = binding.quantityTextInputEditText.text.toString()
        var productQuantity = 1
        if (productQuantityStr.isNotEmpty())
            productQuantity = productQuantityStr.toInt()
        val productMeasureUnit = binding.measurementUnitAutoComplete.text.toString()
        val productCategory = binding.categoryAutoComplete.text.toString()
        val productBrand = binding.productBrandInput.text.toString()

        if (shoppingListId == null)
            addProductToMainList(
                productName,
                productDescription,
                productQuantity,
                productMeasureUnit,
                productCategory,
                productBrand
            )
        else
            addProductToSpecificList(
                productName,
                productDescription,
                productQuantity,
                productMeasureUnit,
                productCategory,
                productBrand
            )

    }


    private fun addProductToMainList(
        productName: String,
        productDescription: String,
        productQuantity: Int,
        productMeasureUnit: String,
        productCategory: String,
        productBrand: String
    ) {
        if (productsViewModel.productWithSameNameAlreadyExists(productName, productBrand)) {
            binding.productNameTextLayout.error =
                getString(R.string.product_with_the_same_name_already_exists)
            binding.productBrandTextLayout.error = " "
            return
        }

        addNewProduct(
            productName,
            productDescription,
            productQuantity,
            productMeasureUnit,
            productCategory,
            productBrand
        )

        findNavController().navigate(
            R.id.action_addProductFragment_to_nav_products
        )
    }

    private fun addProductToSpecificList(
        productName: String,
        productDescription: String,
        productQuantity: Int,
        productMeasureUnit: String,
        productCategory: String,
        productBrand: String
    ) {
        if (productsViewModel.productAlreadyExistsInTheShoppingList(
                productName,
                productBrand,
                shoppingListId!!
            )
        ) {
            binding.productNameTextLayout.error = getString(R.string.product_already_exists_on_list)
            binding.productBrandTextLayout.error = " "
            return
        }

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
                measurementUnitsViewModel.getMeasurementUnitByDesignation(productMeasureUnit)?.measureUnitId

        var productCategoryId: Long? = null
        if (productCategory.isNotEmpty())
            productCategoryId =
                categoriesViewModel.getCategoryByDesignation(productCategory)?.categoryId

        productsViewModel.insertNewProduct(
            productName,
            productDescription,
            productQuantity,
            productMeasureUnitId,
            productCategoryId,
            shoppingListId,
            productBrand,
            null, // TODO
            filePath
        )
    }
}