package me.jalxp.easylist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShoppingList(
    var title: String
) {
    @PrimaryKey(autoGenerate = true)
    var shoppingListId: Long = 0L
}