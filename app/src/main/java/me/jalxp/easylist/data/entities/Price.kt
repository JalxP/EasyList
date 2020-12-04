package me.jalxp.easylist.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Price(
    val value: Double,
    val productId: Long
) {
    @PrimaryKey(autoGenerate = true)
    var priceId: Long = 0

    override fun toString(): String = "%.2f".format(value)
}
