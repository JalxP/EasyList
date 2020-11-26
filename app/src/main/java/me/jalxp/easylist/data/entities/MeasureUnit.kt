package me.jalxp.easylist.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MeasureUnit(
    var designation: String
) {
    @PrimaryKey(autoGenerate = true)
    var measureUnitId: Long = 0L
}
