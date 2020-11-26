package me.jalxp.easylist.data.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import me.jalxp.easylist.data.entities.MeasureUnit

@Dao
interface MeasureUnitDao {
    @Query("SELECT * FROM measureunit")
    fun getAllMeasureUnits(): LiveData<List<MeasureUnit>>

    @Query("SELECT * FROM measureunit WHERE measureUnitId == :measureUnitId")
    fun getMeasureUnitById(measureUnitId: Long) : LiveData<MeasureUnit>

    @Insert
    fun insertMeasureUnit(measureUnit: MeasureUnit)

    @Delete
    fun deleteCategory(measureUnit: MeasureUnit)
}