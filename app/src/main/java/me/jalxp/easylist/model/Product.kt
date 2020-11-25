package me.jalxp.easylist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    var name: String,
    var description: String,
    var quantityId: Long?,
    var categoryId: Long,
    var brand: String?,
    var barCode: Double?,
    var imagePath: String?
) {
    @PrimaryKey(autoGenerate = true)
    var productId: Long = 0L
}