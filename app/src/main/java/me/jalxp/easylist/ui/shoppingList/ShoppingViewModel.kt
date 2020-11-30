package me.jalxp.easylist.ui.shoppingList

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.jalxp.easylist.data.*
import me.jalxp.easylist.data.entities.ShoppingList
import me.jalxp.easylist.data.daos.ShoppingListDao
import java.util.concurrent.Executors

class ShoppingViewModel(val dataSource: ShoppingListDao) : ViewModel() {
    val shoppingListsLiveData = dataSource.getAllShoppingLists()

    private val executorService = Executors.newSingleThreadExecutor()

    fun insertNewShoppingList(listTitle: String, baseShoppingList: ShoppingList?) {
        // TODO
    }

    fun insertNewShoppingList(listTitle: String) {
        executorService.execute {
            dataSource.insertShoppingList(ShoppingList(listTitle))
        }
    }

    fun removeShoppingList(shoppingList: ShoppingList) {
        executorService.execute {
            dataSource.deleteShoppingList(shoppingList)
        }
    }
}

class ShoppingListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShoppingViewModel(
            AppDatabase.getInstance(context).shoppingListsDao()
        ) as T
    }
}

