package me.jalxp.easylist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.jalxp.easylist.data.entities.Category
import me.jalxp.easylist.databinding.ListItemBinding

class CategoriesListAdapter(
    private val onItemClick: (Category) -> Unit,
    private val onEditClick: (Category) -> Unit,
    private val onDeleteClick: (Category) -> Unit
) :
    ListAdapter<Category, CategoriesListAdapter.CategoryListViewHolder>(CategoryListDiffCallback) {

    private lateinit var binding: ListItemBinding

    class CategoryListViewHolder(
        binding: ListItemBinding,
        val onItemClick: (Category) -> Unit,
        val onEditClick: (Category) -> Unit,
        val onDeleteClick: (Category) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val itemTitle = binding.itemTitle
        private var currentCategory: Category? = null

        init {
            itemView.setOnClickListener {
                currentCategory?.let {
                    onItemClick(it)
                }
            }
            binding.editImageButton.setOnClickListener{
                currentCategory?.let {
                    onEditClick(it)
                }
            }
            binding.deleteImageButton.setOnClickListener {
                currentCategory?.let {
                    onDeleteClick(it)
                }
            }
        }
        fun bind(category: Category) {
            currentCategory = category
            itemTitle.text = category.designation
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryListViewHolder(binding, onItemClick, onEditClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }
}

object CategoryListDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.categoryId == newItem.categoryId
    }

}
