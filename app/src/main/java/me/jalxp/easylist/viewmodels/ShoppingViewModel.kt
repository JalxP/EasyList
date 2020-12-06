package me.jalxp.easylist.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.jalxp.easylist.data.*
import me.jalxp.easylist.data.entities.ShoppingList
import me.jalxp.easylist.data.daos.ShoppingListDao
import java.util.concurrent.Executors

class ShoppingViewModel(private val dataSource: ShoppingListDao) : ViewModel() {
    val shoppingListsLiveData = dataSource.getAllShoppingLists()

    private val executorService = Executors.newSingleThreadExecutor()

    fun insertNewShoppingList(listTitle: String) {
            dataSource.insertShoppingList(ShoppingList(listTitle))
    }

    fun getAllShoppingListsNonLive() : List<ShoppingList> {
        return dataSource.getAllShoppingListsNonLive()
    }

    fun getShoppingListById(shoppingListId: Long): ShoppingList {
        return dataSource.getShoppingListById(shoppingListId)
    }

    fun getShoppingListIdFromName(shoppingListName: String) : Long? {
        val list = dataSource.getShoppingListByTitle(shoppingListName)
        if (list.isEmpty())
            return null
        return list.first().shoppingListId
    }

    fun removeShoppingList(shoppingList: ShoppingList) {
        executorService.execute {
            dataSource.deleteShoppingList(shoppingList)
        }
    }

    fun shoppingListAlreadyExists(listTitle: String) : Boolean{
        return dataSource.getShoppingListByTitle(listTitle).isNotEmpty()
    }
}

class ShoppingListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShoppingViewModel(
            AppDatabase.getInstance(context).shoppingListsDao()
        ) as T
    }
}

