package me.jalxp.easylist.data.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import me.jalxp.easylist.data.entities.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM category WHERE categoryId == :categoryId")
    fun getCategoryById(categoryId: Long) : LiveData<Category>

    @Query("SELECT * FROM category WHERE designation == :categoryDesignation")
    fun getCategoryByDesignation(categoryDesignation: String) : LiveData<Category>

    @Insert
    fun insertCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)
}