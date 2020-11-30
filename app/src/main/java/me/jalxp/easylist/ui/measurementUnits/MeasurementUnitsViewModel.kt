package me.jalxp.easylist.ui.measurementUnits

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.jalxp.easylist.data.AppDatabase
import me.jalxp.easylist.data.daos.MeasureUnitDao
import me.jalxp.easylist.data.entities.MeasureUnit
import java.util.concurrent.Executors

class MeasurementUnitsViewModel(val dataSource: MeasureUnitDao) : ViewModel() {
    val measurementUnitsLiveData = dataSource.getAllMeasureUnits()

    private val executorService = Executors.newSingleThreadExecutor()

    fun insertNewMeasurementUnit(designation: String) {
        executorService.execute {
            dataSource.insertMeasureUnit(MeasureUnit(designation))
        }
    }

    fun getMeasurementUnitById(measurementUnitId: Long) : MeasureUnit {
        return dataSource.getMeasureUnitById(measurementUnitId)
    }

    fun getMeasurementUnitByDesignation(measurementUnitDesignation: String) : MeasureUnit {
        return dataSource.getMeasurementUnitByDesignation(measurementUnitDesignation)
    }
}

class MeasurementUnitsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MeasurementUnitsViewModel(
            AppDatabase.getInstance(context).measureUnitsDao()
        ) as T
    }
}