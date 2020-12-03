package me.jalxp.easylist.ui.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import me.jalxp.easylist.R
import me.jalxp.easylist.databinding.FragmentProductsBinding

class ProductsFragment : Fragment() {

    private lateinit var binding: FragmentProductsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* View Binding */
        binding = FragmentProductsBinding.inflate(inflater, container, false)

        /* Attach Product Container Fragment */
        val productsContainerFragment = ProductsContainerFragment()
        val productContainerArguments = Bundle().apply {
            putInt(VIEW_TYPE, SHOW_PRODUCTS_ALL)
        }
        productsContainerFragment.arguments = productContainerArguments

        parentFragmentManager.beginTransaction().add(
            R.id.products_frame, productsContainerFragment
        ).commit()


        binding.addProductFab.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_nav_products_to_addProductFragment, arguments)
        }

        return binding.root
    }
}