package com.devlens.domain.models

data class DeveloperActivity(
    val id: Long = 0,
    val title: String,
    val description: String,
    val minutesSpent: Int,
    val type: ActivityType,
    val tags: List<String>,
    val selfPerceivedVisibility: Int,
    val activityDate: Long,
    val createdAt: Long,
    val analysis: AiAnalysis
)
