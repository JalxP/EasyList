package me.jalxp.easylist.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import me.jalxp.easylist.model.Product
import me.jalxp.easylist.model.ShoppingList
import me.jalxp.easylist.model.ShoppingListProductCrossRef

data class ShoppingListWithProducts(
    @Embedded val shoppingList: ShoppingList,
    @Relation(
        parentColumn = "shoppingListId",
        entityColumn = "productId",
        associateBy = Junction(ShoppingListProductCrossRef::class)
    )
    val products: List<Product>
)