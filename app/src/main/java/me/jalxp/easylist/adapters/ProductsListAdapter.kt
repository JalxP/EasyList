package me.jalxp.easylist.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.jalxp.easylist.data.entities.MeasureUnit
import me.jalxp.easylist.databinding.ProductItemBinding
import me.jalxp.easylist.data.entities.Product
import me.jalxp.easylist.ui.categories.CategoriesViewModel
import me.jalxp.easylist.ui.measurementUnits.MeasurementUnitsViewModel

class ProductsListAdapter(
    private val categoriesViewModel: CategoriesViewModel,
    private val measurementUnitsViewModel: MeasurementUnitsViewModel,
    private val onItemClick: (Product) -> Unit
) : ListAdapter<Product, ProductsListAdapter.ProductViewHolder>(ProductListDiffCallback) {

    private lateinit var binding: ProductItemBinding

    class ProductViewHolder(
        private val binding: ProductItemBinding,
        private val categoriesViewModel: CategoriesViewModel,
        private val measurementUnitsViewModel: MeasurementUnitsViewModel,
        private val onItemClick: (Product) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentProduct: Product? = null

        init {
            itemView.setOnClickListener {
                currentProduct?.let {
                    onItemClick(it)
                }
            }
        }

        fun bind(product: Product) {
            currentProduct = product
            binding.productNameTextView.text = product.name

            // Check if product has associated quantity and measurement units
            var quantityText = if (product.quantity != null) product.quantity.toString() else ""
            if (product.measureUnitId != null) {
                val measureUnit =
                    measurementUnitsViewModel.getMeasurementUnitById(product.measureUnitId!!)
                quantityText += " " + measureUnit.designation
            }
            binding.productQuantityTextView.text = quantityText

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(
            binding,
            categoriesViewModel,
            measurementUnitsViewModel,
            onItemClick
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}

object ProductListDiffCallback : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.productId == newItem.productId
    }
}