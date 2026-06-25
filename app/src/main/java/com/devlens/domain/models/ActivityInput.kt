package com.devlens.domain.models

data class ActivityInput(
    val title: String,
    val description: String,
    val minutesSpent: Int,
    val type: ActivityType,
    val tags: List<String>,
    val selfPerceivedVisibility: Int,
    val activityDate: Long
)
