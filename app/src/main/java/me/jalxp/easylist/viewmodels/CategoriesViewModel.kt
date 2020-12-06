package me.jalxp.easylist.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.jalxp.easylist.data.AppDatabase
import me.jalxp.easylist.data.daos.CategoryDao
import me.jalxp.easylist.data.entities.Category
import java.util.concurrent.Executors

class CategoriesViewModel(val dataSource: CategoryDao) : ViewModel() {
    val categoriesLiveData = dataSource.getAllCategories()

    private val executorService = Executors.newSingleThreadExecutor()

    fun insertNewCategory(categoryDesignation: String) {
        if (categoryDesignation.isNullOrEmpty())
            return
        executorService.execute {
            dataSource.insertCategory(Category(categoryDesignation))
        }
    }

    fun getAllCategoriesNonLive() : List<Category> {
        return dataSource.getAllCategoriesNonLive()
    }

    fun getCategoryByDesignation(categoryDesignation: String) : Category {
        return dataSource.getCategoryByDesignation(categoryDesignation)
    }

    fun getCategoryById(categoryId: Long) : Category {
        return dataSource.getCategoryById(categoryId)
    }

    fun deleteCategory(category: Category) {

        // TODO remove association with items

        executorService.execute {
            dataSource.deleteCategory(category)
        }
    }
}

class CategoriesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoriesViewModel(
            AppDatabase.getInstance(context).categoriesDao()
        ) as T
    }
}