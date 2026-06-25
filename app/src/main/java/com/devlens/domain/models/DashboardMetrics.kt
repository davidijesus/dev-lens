package com.devlens.domain.models

data class DashboardMetrics(
    val totalActivities: Int = 0,
    val averageEffective: Int = 0,
    val averagePerceived: Int = 0,
    val invisibleHighValueCount: Int = 0,
    val overestimatedCount: Int = 0,
    val criticalActivities: List<DeveloperActivity> = emptyList(),
    val categoryDistribution: Map<MisalignmentCategory, Int> = emptyMap()
)
