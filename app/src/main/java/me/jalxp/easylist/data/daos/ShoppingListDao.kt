package me.jalxp.easylist.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import me.jalxp.easylist.data.entities.ShoppingList

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shoppinglist")
    fun getAllShoppingLists(): LiveData<List<ShoppingList>>

    @Query("SELECT * FROM shoppinglist WHERE shoppingListId == :shoppingListId")
    fun getShoppingListById(shoppingListId: Long) : LiveData<ShoppingList>

    @Insert
    fun insertShoppingList(shoppingList: ShoppingList)

    @Delete
    fun deleteShoppingList(shoppingList: ShoppingList)
}