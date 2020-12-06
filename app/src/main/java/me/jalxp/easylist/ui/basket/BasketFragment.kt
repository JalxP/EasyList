package me.jalxp.easylist.ui.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import me.jalxp.easylist.R
import me.jalxp.easylist.databinding.FragmentBasketBinding
import me.jalxp.easylist.ui.products.*
import me.jalxp.easylist.viewmodels.ProductsViewModel
import me.jalxp.easylist.viewmodels.ProductsViewModelFactory

class BasketFragment : Fragment() {
    private lateinit var binding: FragmentBasketBinding
    private val productsViewModel: ProductsViewModel by activityViewModels {
        ProductsViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBasketBinding.inflate(inflater, container, false)


        val productsContainerFragment = ProductsContainerFragment()
        val productFragmentArguments = Bundle().apply {
            putInt(VIEW_TYPE, SHOW_PRODUCTS_ON_CART)
        }
        productsContainerFragment.arguments = productFragmentArguments

        parentFragmentManager.beginTransaction().add(
            R.id.products_frame, productsContainerFragment
        ).commit()

        binding.clearBasketFloatingActionButton.setOnClickListener {
            clearBasketClick()
        }

        return binding.root
    }

    private fun clearBasketClick() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.clear_basket_question))
            .setMessage(resources.getString(R.string.clear_basket_message))
            .setNeutralButton(resources.getString(R.string.dialogue_cancel)) { dialog, which ->
                Snackbar.make(binding.root, "Canceled", Snackbar.LENGTH_LONG).show()
            }
            .setPositiveButton(resources.getString(R.string.clear_basket_confirm)) { dialog, which ->
                Snackbar.make(binding.root, "Confirmed", Snackbar.LENGTH_LONG).show()
                clearBasket()
            }
            .show()
    }

    private fun clearBasket() {
        productsViewModel.getProductsOnCartNonLive().forEach {
            it.onCart = false
            productsViewModel.updateProduct(it)
        }
    }
}