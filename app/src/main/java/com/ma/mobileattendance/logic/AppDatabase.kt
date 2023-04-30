package com.ma.mobileattendance.logic

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ma.mobileattendance.logic.dao.StaffDao
import com.ma.mobileattendance.logic.model.Staff

@Database(version = 1, entities = [Staff::class],exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun staffDao(): StaffDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let { return it }
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app_database")
                .build()
                .apply { instance = this }
        }
    }
}