package me.jalxp.easylist.model

import android.content.Context
import androidx.room.Room

class Data {
    companion object {
        private var dao : DataAccessObject? = null

        fun getData(context: Context) : DataAccessObject {
            if (dao == null) {
                dao = Room.databaseBuilder(context, AppDatabase::class.java, "database_test.db")
                    .build().dataAccessObject()
            }
            return dao as DataAccessObject
        }
    }
}