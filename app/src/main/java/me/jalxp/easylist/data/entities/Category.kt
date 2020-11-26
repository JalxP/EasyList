package me.jalxp.easylist.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    var designation: String
) {
    @PrimaryKey(autoGenerate = true)
    var categoryId: Long = 0
}