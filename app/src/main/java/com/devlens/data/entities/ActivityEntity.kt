package com.devlens.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val minutesSpent: Int,
    val type: String,
    val tags: String,
    val selfPerceivedVisibility: Int,
    val activityDate: Long,
    val createdAt: Long,
    val effectiveScore: Int,
    val perceivedScore: Int,
    val gap: Int,
    val category: String,
    val confidence: Float,
    val explanation: String,
    val effectiveFactors: String,
    val perceivedFactors: String,
    val recommendations: String,
    val analyzedAt: Long
)
