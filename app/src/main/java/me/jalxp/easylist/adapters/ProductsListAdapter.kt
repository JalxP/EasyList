package me.jalxp.easylist.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.jalxp.easylist.data.entities.Product
import me.jalxp.easylist.databinding.ProductItemBinding
import me.jalxp.easylist.viewmodels.CategoriesViewModel
import me.jalxp.easylist.viewmodels.MeasurementUnitsViewModel

class ProductsListAdapter(
    private val categoriesViewModel: CategoriesViewModel,
    private val measurementUnitsViewModel: MeasurementUnitsViewModel,
    private val onItemClick: (Product) -> Unit,
    private val onLongItemClick: (Product) -> Unit
) : ListAdapter<Product, ProductsListAdapter.ProductViewHolder>(ProductListDiffCallback) {

    private lateinit var binding: ProductItemBinding

    class ProductViewHolder(
        private val binding: ProductItemBinding,
        private val categoriesViewModel: CategoriesViewModel,
        private val measurementUnitsViewModel: MeasurementUnitsViewModel,
        private val onItemClick: (Product) -> Unit,
        private val onLongItemClick: (Product) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentProduct: Product? = null
        private var width = itemView .measuredWidth
        private var height = itemView.measuredHeight

        init {
            itemView.setOnClickListener {
                currentProduct?.let {
                    onItemClick(it)
                }
            }
            itemView.setOnLongClickListener {
                currentProduct?.let {
                    onLongItemClick(it)
                }
                return@setOnLongClickListener true
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
            if (product.onCart)
                binding.productNameTextView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#43A047"));
            else
                binding.productNameTextView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFD149"));
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(
            binding,
            categoriesViewModel,
            measurementUnitsViewModel,
            onItemClick,
            onLongItemClick
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
        return oldItem.productId == newItem.productId && oldItem.onCart == newItem.onCart
    }
}