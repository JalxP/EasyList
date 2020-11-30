package me.jalxp.easylist.ui.products

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.jalxp.easylist.data.AppDatabase
import me.jalxp.easylist.data.daos.ProductDao
import me.jalxp.easylist.data.entities.Product
import java.util.concurrent.Executors


class ProductsViewModel(val dataSource: ProductDao) : ViewModel() {
    val productsLiveData = dataSource.getAllProducts()

    private val executorService = Executors.newSingleThreadExecutor()

    fun insertNewProduct(
        name: String,
        description: String?,
        quantity: Int,
        measureUnitId: Long?,
        categoryId: Long?,
        shoppingListId: Long,
        brand: String?,
        barCode: Double?,
        imagePath: String?
    ) {
        val product = Product(name, description, quantity, measureUnitId, categoryId, shoppingListId, brand, barCode, imagePath)
        executorService.execute {
            dataSource.insertProduct(product)
        }
    }

    fun productAlreadyExistsInTheShoppingList(productName: String, shoppingListId: Long) : Boolean {
        dataSource.getProductsByShoppingListIdNonLive(shoppingListId).forEach {
            if (it.name.equals(productName, true))
                return true
        }
        return false
    }

    fun getProductsByShoppingListId(shoppingListId: Long) : LiveData<List<Product>> {
        return dataSource.getProductsByShoppingListId(shoppingListId)
    }
}

class ProductsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductsViewModel(
            AppDatabase.getInstance(context).productsDao()
        ) as T
    }

}