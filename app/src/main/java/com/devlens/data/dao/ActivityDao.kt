package com.devlens.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devlens.data.entities.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activities ORDER BY activityDate DESC, createdAt DESC")
    fun observeActivities(): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities ORDER BY activityDate DESC, createdAt DESC")
    suspend fun getActivities(): List<ActivityEntity>

    @Query("SELECT * FROM activities WHERE id = :id")
    suspend fun findActivity(id: Long): ActivityEntity?

    @Query("SELECT COUNT(*) FROM activities")
    suspend fun countActivities(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(entity: ActivityEntity): Long
}
