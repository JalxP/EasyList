package me.jalxp.easylist.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MeasureUnit(
    var designation: String
) {
    @PrimaryKey(autoGenerate = true)
    var measureUnitId: Long = 0L

    override fun toString() = designation

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MeasureUnit

        if (designation != other.designation) return false

        return true
    }

    override fun hashCode(): Int {
        return designation.hashCode()
    }
}
