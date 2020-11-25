package me.jalxp.easylist.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [
    Product::class,
    Quantity::class,
    Category::class,
    ShoppingList::class,
    ShoppingListProductCrossRef::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun dataAccessObject() : DataAccessObject
}