package me.jalxp.easylist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Quantity(
    var designation: String,
    var value: Double
) {
    @PrimaryKey(autoGenerate = true)
    var quantityId: Long = 0L
}
