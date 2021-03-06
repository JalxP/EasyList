package me.jalxp.easylist.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import me.jalxp.easylist.data.entities.Category
import me.jalxp.easylist.data.entities.Product
import me.jalxp.easylist.databinding.CategoryHeaderBinding
import me.jalxp.easylist.databinding.ProductItemBinding


class ExpandableCategoriesAdapter(
    private val context: Context,
    private val headers: List<Category>,
    private val details: MutableMap<Category, MutableList<Product>>,
    private val onItemClick: (Product) -> Unit,
    private val onLongItemClick: (Product) -> Unit
) : BaseExpandableListAdapter() {

    private lateinit var childBinding: ProductItemBinding
    private lateinit var groupBinding: CategoryHeaderBinding

    override fun getGroupCount(): Int {
        return headers.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return details[headers[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return headers[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return details[headers[groupPosition]]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        groupBinding = CategoryHeaderBinding.inflate(layoutInflater)

        val category = getGroup(groupPosition) as Category
        groupBinding.categoryTitle.text = category.designation

        return groupBinding.root
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        childBinding = ProductItemBinding.inflate(layoutInflater)

        val product = getChild(groupPosition, childPosition) as Product
        childBinding.productNameTextView.text = product.name
        childBinding.productQuantityTextView.text = product.quantity.toString()
        if (product.onCart)
            childBinding.productNameTextView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#43A047"));
        else
            childBinding.productNameTextView.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFD149"));

        childBinding.root.setOnLongClickListener {
            onLongItemClick(product)
            true
        }

        childBinding.root.setOnClickListener {
            onItemClick(product)
            true
        }

        return childBinding.root
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

}