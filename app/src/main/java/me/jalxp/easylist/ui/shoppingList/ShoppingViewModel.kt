package me.jalxp.easylist.ui.shoppingList

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.jalxp.easylist.model.Data
import me.jalxp.easylist.model.DataAccessObject
import me.jalxp.easylist.model.ShoppingList
import java.util.concurrent.Executors

class ShoppingViewModel(val dataSource: DataAccessObject): ViewModel() {
    val shoppingListsLiveData = dataSource.getAllShoppingLists()

    private val executorService = Executors.newSingleThreadExecutor()

    fun insertNewShoppingList(listTitle: String, baseShoppingList: ShoppingList?) {
        // TODO create new shopping list based on baseShoppingList
        if (baseShoppingList == null) {
            executorService.execute {
                dataSource.insertShoppingList(ShoppingList(listTitle))
            }
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
            Data.getData(context)
        ) as T
    }
}

