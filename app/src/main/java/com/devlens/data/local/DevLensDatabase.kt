package com.devlens.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devlens.data.dao.ActivityDao
import com.devlens.data.entities.ActivityEntity

@Database(
    entities = [ActivityEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DevLensDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
}
