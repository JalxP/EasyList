package me.jalxp.easylist.ui.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import me.jalxp.easylist.R
import me.jalxp.easylist.databinding.FragmentProductDetailBinding
import me.jalxp.easylist.ui.shoppingList.EXTRA_LIST_ID
import me.jalxp.easylist.ui.shoppingList.EXTRA_PRODUCT_ID

class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailBinding.inflate(layoutInflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val productId = arguments?.getLong(EXTRA_PRODUCT_ID)
        val shoppingListId = arguments?.getLong(EXTRA_LIST_ID)
        Snackbar.make(binding.root, "ProductId: $productId, ShoppingListId: $shoppingListId", Snackbar.LENGTH_LONG).show()
    }
}