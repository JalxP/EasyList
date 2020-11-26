package me.jalxp.easylist.data.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import me.jalxp.easylist.data.entities.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT * FROM product WHERE productId == :productId")
    fun getProductById(productId: Long) : LiveData<Product>

    @Insert
    fun insertProduct(product: Product)

    @Delete
    fun deleteProduct(product: Product)
}