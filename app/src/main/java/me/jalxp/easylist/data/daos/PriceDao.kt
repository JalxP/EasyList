package me.jalxp.easylist.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import me.jalxp.easylist.data.entities.Price

@Dao
interface PriceDao {

    @Query("SELECT value FROM price WHERE productId == :productId ORDER BY value")
    fun getAllPricesByProductId(productId: Long) : List<Double>

    @Query("SELECT MAX(value) FROM price WHERE productId == :productId")
    fun getMaxPriceByProductId(productId: Long) : Double

    @Query("SELECT MIN(value) FROM price WHERE productId == :productId")
    fun getMinPriceByProductId(productId: Long) : Double

    @Insert
    fun insertPrice(price: Price)

    @Delete
    fun deletePrice(price: Price)
}