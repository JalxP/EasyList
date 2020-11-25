package me.jalxp.easylist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    var designation: String
) {
    @PrimaryKey(autoGenerate = true)
    var categoryId: Long = 0L
}