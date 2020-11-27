package me.jalxp.easylist.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    var name: String,
    var description: String?,
    var quantity: Int?,
    var measureUnitId: Long?,
    var categoryId: Long?,
    var shoppingListId: Long,
    var brand: String?,
    var barCode: Double?,
    var imagePath: String?
) {
    @PrimaryKey(autoGenerate = true)
    var productId: Long = 0L
}