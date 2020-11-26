package me.jalxp.easylist.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.jalxp.easylist.databinding.ListItemBinding
import me.jalxp.easylist.data.entities.ShoppingList

class ShoppingListsAdapter(
    private val onItemClick: (ShoppingList) -> Unit,
    private val onEditClick: (ShoppingList) -> Unit,
    private val onDeleteClick: (ShoppingList) -> Unit
) :
    ListAdapter<ShoppingList, ShoppingListsAdapter.ShoppingListViewHolder>(ShoppingListDiffCallback) {

    private lateinit var binding: ListItemBinding

    class ShoppingListViewHolder(
        binding: ListItemBinding,
        val onItemClick: (ShoppingList) -> Unit,
        val onEditClick: (ShoppingList) -> Unit,
        val onDeleteClick: (ShoppingList) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private val listTitleTextView = binding.itemTitle
        private var currentList: ShoppingList? = null

        init {
            itemView.setOnClickListener {
                currentList?.let {
                    onItemClick(it)
                }
            }
            binding.editImageButton.setOnClickListener{
                currentList?.let {
                    onEditClick(it)
                }
            }
            binding.deleteImageButton.setOnClickListener {
                currentList?.let {
                    onDeleteClick(it)
                }
            }
        }

        fun bind(shoppingList: ShoppingList) {
            currentList = shoppingList
            listTitleTextView.text = shoppingList.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoppingListViewHolder(binding, onItemClick, onEditClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val shoppingList = getItem(position)
        holder.bind(shoppingList)
    }
}

object ShoppingListDiffCallback : DiffUtil.ItemCallback<ShoppingList>() {

    override fun areItemsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
        return oldItem.shoppingListId == newItem.shoppingListId
    }
}