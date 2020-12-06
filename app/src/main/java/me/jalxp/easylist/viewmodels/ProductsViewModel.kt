package me.jalxp.easylist.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.jalxp.easylist.data.AppDatabase
import me.jalxp.easylist.data.daos.ProductDao
import me.jalxp.easylist.data.entities.Product
import java.util.concurrent.Executors

class ProductsViewModel(private val dataSource: ProductDao) : ViewModel() {

    private val executorService = Executors.newSingleThreadExecutor()

    fun insertNewProduct(
        name: String,
        description: String?,
        quantity: Int,
        measureUnitId: Long?,
        categoryId: Long?,
        shoppingListId: Long?,
        brand: String?,
        barCode: Long?,
        imagePath: String?
    ) {
        val product = Product(name, description, quantity, measureUnitId, categoryId, shoppingListId, brand, barCode, imagePath)
        executorService.execute {
            dataSource.insertProduct(product)
        }
    }

    fun productAlreadyExistsInTheShoppingList(productName: String, productBrand: String, shoppingListId: Long) : Boolean {
        dataSource.getProductsByShoppingListIdNonLive(shoppingListId).forEach {
            if (it.name.equals(productName, true) && it.brand.equals(productBrand, true))
                return true
        }
        return false
    }

    fun productWithSameNameAlreadyExists(productName: String, productBrand: String) : Boolean {
        dataSource.getAllProductsNonLive().forEach {
            if (it.name.equals(productName, true) && it.brand.equals(productBrand, true))
                return true
        }
        return false
    }

    fun getAllProducts() : LiveData<List<Product>> {
        return dataSource.getAllProducts()
    }

    fun getProductById(productId: Long) : Product {
        return dataSource.getProductById(productId)
    }

    fun getProductsByShoppingListId(shoppingListId: Long) : LiveData<List<Product>> {
        return dataSource.getProductsByShoppingListId(shoppingListId)
    }

    fun getProductsByShoppingListIdNonLive(shoppingListId: Long) : List<Product> {
        return dataSource.getProductsByShoppingListIdNonLive(shoppingListId)
    }

    fun getProductsOnCart() : LiveData<List<Product>> {
        return dataSource.getProductsOnCart()
    }

    fun getProductsOnCartNonLive() : List<Product> {
        return dataSource.getProductsOnCartNonLive()
    }

    fun updateProduct(product: Product) {
        dataSource.updateProduct(product)
    }

    fun clearAllAssociationsWithShoppingList(shoppingListId: Long) {
        // TODO Use MutableLiveData instead
        getProductsByShoppingListIdNonLive(shoppingListId).forEach {
            it.shoppingListId = null
            updateProduct(it)
        }
    }
}

class ProductsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductsViewModel(
            AppDatabase.getInstance(context).productsDao()
        ) as T
    }

}