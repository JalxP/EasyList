package me.jalxp.easylist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.jalxp.easylist.data.entities.MeasureUnit
import me.jalxp.easylist.databinding.ListItemBinding

class MeasurementUnitsListAdapter(
    private val onItemClick: (MeasureUnit) -> Unit,
    private val onEditClick: (MeasureUnit) -> Unit,
    private val onDeleteClick: (MeasureUnit) -> Unit
) :
    ListAdapter<MeasureUnit, MeasurementUnitsListAdapter.MeasurementUnitListViewHolder>(
        MeasurementUnitListDiffCallback
    ) {

    private lateinit var binding: ListItemBinding

    class MeasurementUnitListViewHolder(
        binding: ListItemBinding,
        val onItemClick: (MeasureUnit) -> Unit,
        val onEditClick: (MeasureUnit) -> Unit,
        val onDeleteClick: (MeasureUnit) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val itemTitle = binding.itemTitle
        private var currentMeasureUnit: MeasureUnit? = null

        init {
            itemView.setOnClickListener {
                currentMeasureUnit?.let {
                    onItemClick(it)
                }
            }
            binding.editImageButton.setOnClickListener {
                currentMeasureUnit?.let {
                    onEditClick(it)
                }
            }
            binding.deleteImageButton.setOnClickListener {
                currentMeasureUnit?.let {
                    onDeleteClick(it)
                }
            }
        }

        fun bind(measureUnit: MeasureUnit) {
            currentMeasureUnit = measureUnit
            itemTitle.text = measureUnit.designation
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MeasurementUnitListViewHolder {
        binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeasurementUnitListViewHolder(
            binding,
            onItemClick,
            onEditClick,
            onDeleteClick
        )
    }

    override fun onBindViewHolder(holder: MeasurementUnitListViewHolder, position: Int) {
        val measureUnit = getItem(position)
        holder.bind(measureUnit)
    }
}

object MeasurementUnitListDiffCallback : DiffUtil.ItemCallback<MeasureUnit>() {
    override fun areItemsTheSame(oldItem: MeasureUnit, newItem: MeasureUnit): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MeasureUnit, newItem: MeasureUnit): Boolean {
        return oldItem.measureUnitId == newItem.measureUnitId
    }
}


