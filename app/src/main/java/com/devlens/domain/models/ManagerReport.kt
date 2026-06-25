package com.devlens.domain.models

data class ManagerReport(
    val weeklySummary: String,
    val highImpactActivities: List<DeveloperActivity>,
    val invisibleActivities: List<DeveloperActivity>,
    val alignmentSuggestions: List<String>,
    val shareableSummary: String
)
