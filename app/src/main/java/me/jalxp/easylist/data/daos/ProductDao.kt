package me.jalxp.easylist.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import me.jalxp.easylist.data.entities.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM product GROUP BY name, brand")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT * FROM product")
    fun getAllProductsNonLive(): List<Product>

    @Query("SELECT * FROM product WHERE productId == :productId")
    fun getProductById(productId: Long) : Product

    @Query("SELECT * FROM product WHERE shoppingListId == :shoppingListId")
    fun getProductsByShoppingListId(shoppingListId: Long) : LiveData<List<Product>>

    @Query("SELECT * FROM product WHERE shoppingListId == :shoppingListId")
    fun getProductsByShoppingListIdNonLive(shoppingListId: Long) : List<Product>

    @Query("SELECT * FROM product WHERE onCart == 1")
    fun getProductsOnCart(): LiveData<List<Product>>

    @Query("SELECT * FROM product WHERE onCart == 1")
    fun getProductsOnCartNonLive(): List<Product>

    @Query("SELECT EXISTS (SELECT * FROM product WHERE onCart == 1)")
    fun isCartEmpty() : LiveData<Boolean>

    @Update
    fun updateProduct(product: Product)

    @Insert
    fun insertProduct(product: Product)

    @Delete
    fun deleteProduct(product: Product)
}