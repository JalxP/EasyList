package me.jalxp.easylist.ui.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.jalxp.easylist.R
import me.jalxp.easylist.databinding.FragmentBasketBinding
import me.jalxp.easylist.ui.shoppingList.EXTRA_LIST_ID

class BasketFragment : Fragment() {
    private lateinit var binding: FragmentBasketBinding

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

        return binding.root
    }
}