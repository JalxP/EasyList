package me.jalxp.easylist.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import me.jalxp.easylist.R
import me.jalxp.easylist.adapters.CategoriesListAdapter
import me.jalxp.easylist.data.entities.Category
import me.jalxp.easylist.databinding.FragmentCategoriesBinding
import me.jalxp.easylist.viewmodels.CategoriesViewModel
import me.jalxp.easylist.viewmodels.CategoriesViewModelFactory

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private val categoriesViewModel: CategoriesViewModel by activityViewModels {
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
        binding.createCategoryFab.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_nav_categories_to_addCategoryFragment)
        }

        return binding.root
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
}