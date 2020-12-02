package me.jalxp.easylist.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShoppingList(
    var title: String
) {
    @PrimaryKey(autoGenerate = true)
    var shoppingListId: Long = 0L

    override fun toString(): String = title
}