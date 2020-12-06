package me.jalxp.easylist.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.jalxp.easylist.data.AppDatabase
import me.jalxp.easylist.data.daos.PriceDao
import me.jalxp.easylist.data.entities.Price

class PricesViewModel(private val dataSource: PriceDao) : ViewModel() {

    fun insertPrice(value: Double, productId: Long) {
        dataSource.insertPrice(Price(value, productId))
    }

    fun getAllPricesByProductId(productId: Long) : List<Double> {
        return dataSource.getAllPricesByProductId(productId)
    }

    fun getMinPriceByProductId(productId: Long) : Double {
        return dataSource.getMinPriceByProductId(productId)
    }

    fun getMaxPriceByProductId(productId: Long) : Double {
        return dataSource.getMaxPriceByProductId(productId)
    }

}

class PricesViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PricesViewModel(
            AppDatabase.getInstance(context).priceDao()
        ) as T
    }

}