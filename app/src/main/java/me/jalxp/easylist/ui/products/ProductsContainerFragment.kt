package me.jalxp.easylist.ui.products

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import me.jalxp.easylist.R
import me.jalxp.easylist.adapters.ProductsListAdapter
import me.jalxp.easylist.data.entities.Product
import me.jalxp.easylist.databinding.FragmentContainerProductsBinding
import me.jalxp.easylist.databinding.PurchaseDialogueBinding
import me.jalxp.easylist.viewmodels.CategoriesViewModel
import me.jalxp.easylist.viewmodels.CategoriesViewModelFactory
import me.jalxp.easylist.viewmodels.MeasurementUnitsViewModel
import me.jalxp.easylist.viewmodels.MeasurementUnitsViewModelFactory

import me.jalxp.easylist.ui.shoppingList.EXTRA_LIST_ID
import me.jalxp.easylist.ui.shoppingList.EXTRA_PRODUCT_ID
import me.jalxp.easylist.ui.shoppingList.EXTRA_PRODUCT_NAME
import me.jalxp.easylist.viewmodels.PricesViewModel
import me.jalxp.easylist.viewmodels.PricesViewModelFactory
import me.jalxp.easylist.viewmodels.ProductsViewModel
import me.jalxp.easylist.viewmodels.ProductsViewModelFactory

const val VIEW_TYPE = "view type"
const val SHOW_PRODUCTS_ALL = 1
const val SHOW_PRODUCTS_BY_LIST = 2
const val SHOW_PRODUCTS_ON_CART = 3

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
    private val pricesViewModel: PricesViewModel by activityViewModels {
        PricesViewModelFactory(requireContext())
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
            SHOW_PRODUCTS_ON_CART -> {
                productsViewModel.getProductsOnCart().observe(viewLifecycleOwner, {
                    productsAdapter.submitList(it)
                })
            }
        }

        return binding.root
    }

    private fun adapterOnItemClick(product: Product) {
        val bundle = Bundle().apply {
            putLong(EXTRA_PRODUCT_ID, product.productId)
            putString(EXTRA_PRODUCT_NAME, product.name)
        }

        // Because we have more origins with the same destination
        when (findNavController().currentDestination?.id) {
            R.id.nav_singleListFragment -> {
                findNavController().navigate(
                    R.id.action_singleListFragment_to_productDetailFragment,
                    bundle
                )
            }
            R.id.nav_products -> {
                findNavController().navigate(
                    R.id.action_nav_products_to_productDetailFragment,
                    bundle
                )
            }
            R.id.nav_basketFragment -> {
                findNavController().navigate(
                    R.id.action_nav_basketFragment_to_nav_productDetailFragment,
                    bundle
                )
            }
        }
    }

    private fun adapterOnItemLongClick(product: Product) {

        when (findNavController().currentDestination?.id) {
            R.id.nav_singleListFragment -> {
                product.onCart = !product.onCart
                productsViewModel.updateProduct(product)
                binding.recycleViewList.adapter?.notifyDataSetChanged()

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
            R.id.nav_basketFragment -> {
                registerPurchase(product)
            }
            else -> {
                Log.e("<DEBUG>", "Do not add ${product.name} to shopping cart!")
            }
        }
    }

    private fun registerPurchase(product: Product) {

        val dialogueBinding =
            PurchaseDialogueBinding.inflate(layoutInflater, view as ViewGroup, false)
        val dialogue = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.purchase_price_indication) + product.name)
            .setView(dialogueBinding.root)
            .setPositiveButton(
                getString(R.string.submit_price), null
            )
            .setNegativeButton(getString(R.string.dialogue_cancel), null)
            .show()

        dialogue.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (dialogueBinding.priceTextInputEditText.text.isNullOrEmpty()) {
                dialogueBinding.priceTextInputLayout.error = getString(R.string.need_price_error)
            } else {
                dialogue.dismiss()
                // Process the product price here and remove item from cart and shopping list
                val price = dialogueBinding.priceTextInputEditText.text.toString().toDouble()
                pricesViewModel.insertPrice(price, product.productId)
                product.onCart = false
                product.shoppingListId = null
                productsViewModel.updateProduct(product)
                Snackbar.make(
                    binding.root,
                    getString(R.string.prefix_purchased_registed) + product.name + getString(R.string.posfix_purchased_registed) + price,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}