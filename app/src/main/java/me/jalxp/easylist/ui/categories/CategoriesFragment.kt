package me.jalxp.easylist.ui.categories

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import me.jalxp.easylist.adapters.CategoriesListAdapter
import me.jalxp.easylist.data.entities.Category
import me.jalxp.easylist.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private val newCategoryActivityRequestCode = 1
    private val categoriesViewModel: CategoriesViewModel by viewModels {
        CategoriesViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* View Binding */
        binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)

        /* Recycle View */
        val categoriesListAdapter = CategoriesListAdapter(
            { category -> adapterOnItemClick(category) },
            { category -> adapterOnEditClick(category) },
            { category -> adapterOnDeleteClick(category) }
        )

        with(binding) {
            categoriesRecycleView.adapter = categoriesListAdapter
            categoriesRecycleView.layoutManager =
                LinearLayoutManager(this@CategoriesFragment.context, LinearLayoutManager.VERTICAL, false)
        }

        categoriesViewModel.categoriesLiveData.observe(viewLifecycleOwner, {
            it?.let {
                categoriesListAdapter.submitList(it as MutableList<Category>)
            }
        })

        /* Floating Button */
        binding.createCategoryFab.setOnClickListener {
            createCategoryOnClick()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newCategoryActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->

                val categoryTitle = data.getStringExtra(CATEGORY_DESCRIPTION)
                if (categoryTitle != null)
                    categoriesViewModel.insertNewCategory(categoryTitle)
            }
        }
    }

    private fun adapterOnItemClick(category: Category) {
        // TODO
    }

    private fun adapterOnEditClick(category: Category) {
        // TODO
    }

    private fun adapterOnDeleteClick(category: Category) {

        // TODO check if category has products
        // Remove category from said products
        categoriesViewModel.deleteCategory(category)
    }

    private fun createCategoryOnClick() {
        val intent = Intent(context, AddCategoryActivity::class.java)
        startActivityForResult(intent, newCategoryActivityRequestCode)
    }
}