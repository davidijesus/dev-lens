package com.devlens.domain.repositories

import com.devlens.domain.models.DeveloperActivity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun observeActivities(): Flow<List<DeveloperActivity>>
    suspend fun getActivities(): List<DeveloperActivity>
    suspend fun findActivity(id: Long): DeveloperActivity?
    suspend fun saveActivity(activity: DeveloperActivity): Long
    suspend fun seedIfEmpty()
}
