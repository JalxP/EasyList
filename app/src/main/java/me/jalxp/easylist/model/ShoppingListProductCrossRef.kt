package me.jalxp.easylist.model

import androidx.room.Entity

@Entity(primaryKeys = ["shoppingListId", "productId"])
data class ShoppingListProductCrossRef (
    val shoppingListId: Long,
    val productId: Long
)