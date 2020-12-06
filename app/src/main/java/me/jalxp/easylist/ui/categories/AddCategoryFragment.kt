package me.jalxp.easylist.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import me.jalxp.easylist.R
import me.jalxp.easylist.databinding.FragmentAddCategoryBinding
import me.jalxp.easylist.viewmodels.CategoriesViewModel
import me.jalxp.easylist.viewmodels.CategoriesViewModelFactory

class AddCategoryFragment: Fragment() {

    private lateinit var binding: FragmentAddCategoryBinding
    private val categoriesViewModel: CategoriesViewModel by activityViewModels {
        CategoriesViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCategoryBinding.inflate(inflater, container, false)

        binding.addCategoryButton.setOnClickListener {
            addCategory()
        }

        return binding.root
    }

    private fun addCategory() {

        val categoryTitle = binding.addCategoryTitle.text.toString()

        when {
            categoryTitle.isEmpty() -> {
                binding.categoryTextLayout.error = getString(R.string.need_description_error)
                return
            }
            categoryAlreadyExists(categoryTitle) -> {
                binding.categoryTextLayout.error = getString(R.string.category_already_exists_error)
                return
            }
            else -> {
                categoriesViewModel.insertNewCategory(categoryTitle)
                findNavController().navigate(R.id.action_addCategoryFragment_to_nav_categories)
            }
        }
    }

    private fun categoryAlreadyExists(categoryDesignation: String) : Boolean {
        val categories =  categoriesViewModel.categoriesLiveData.value

        categories?.forEach {
            if (it.designation == categoryDesignation)
                return true
        }
        return false
    }


}