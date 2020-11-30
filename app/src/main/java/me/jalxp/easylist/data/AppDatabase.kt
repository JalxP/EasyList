package me.jalxp.easylist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.jalxp.easylist.data.entities.Category
import me.jalxp.easylist.data.entities.MeasureUnit
import me.jalxp.easylist.data.entities.Product
import me.jalxp.easylist.data.entities.ShoppingList
import me.jalxp.easylist.data.daos.CategoryDao
import me.jalxp.easylist.data.daos.MeasureUnitDao
import me.jalxp.easylist.data.daos.ProductDao
import me.jalxp.easylist.data.daos.ShoppingListDao

@Database(
    entities = [
        ShoppingList::class,
        Product::class,
        Category::class,
        MeasureUnit::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shoppingListsDao(): ShoppingListDao
    abstract fun productsDao(): ProductDao
    abstract fun categoriesDao(): CategoryDao
    abstract fun measureUnitsDao(): MeasureUnitDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "easylist_database.db")
                .allowMainThreadQueries()
                .build()
        }
    }
}