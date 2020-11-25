package me.jalxp.easylist.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DataAccessObject {

    /*
     * Products
     */
    @Query("SELECT * FROM product")
    fun getAllProducts(): List<Product>

    @Query("SELECT * FROM product WHERE categoryId == :categoryId")
    fun loadAllProductsByCategoryId(categoryId: Long): List<Product>

    @Insert
    fun insertAllProducts(vararg products: Product)

    @Delete
    fun deleteProduct(product: Product)

    /*
     * Categories
     */
    @Query("SELECT * FROM category")
    fun getAllCategories(): List<Category>

    @Query("SELECT * FROM category WHERE categoryId == :categoryId")
    fun getCategoryById(categoryId: Long) : Category

    @Insert
    fun insertAllCategories(vararg categories: Category)

    @Delete
    fun deleteCategory(category: Category)


    /*
     * Quantities
     */
    @Query("SELECT * FROM quantity")
    fun getAllQuantities(): List<Quantity>

    @Query("SELECT * FROM quantity WHERE quantityId == :quantityId")
    fun getQuantityById(quantityId: Long) : Quantity

    @Insert
    fun insertAllQuantities(vararg quantities: Quantity)

    @Delete
    fun deleteQuantity(quantity: Quantity)

    /*
     * ShoppingLists
     */
    @Query("SELECT * FROM shoppinglist")
    fun getAllShoppingLists(): LiveData<List<ShoppingList>>

    @Query("SELECT * FROM shoppinglist WHERE shoppingListId == :shoppingListId")
    fun getShoppingListById(shoppingListId: Long) : ShoppingList

    @Transaction
    @Query("SELECT * FROM shoppinglist")
    fun getShoppingListsWithProducts(): LiveData<List<ShoppingListWithProducts>>

    @Transaction
    @Query("SELECT * FROM shoppinglist WHERE shoppingListId == :shoppingListId")
    fun getShoppingListWithProductsByListId(shoppingListId: Long): ShoppingListWithProducts

    @Insert
    fun insertShoppingList(shoppingList: ShoppingList)

    @Delete
    fun deleteShoppingList(shoppingList: ShoppingList)
}