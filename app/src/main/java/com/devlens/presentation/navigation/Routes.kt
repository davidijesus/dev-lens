package com.devlens.presentation.navigation

sealed class Route(val path: String) {
    data object Onboarding : Route("onboarding")
    data object Dashboard : Route("dashboard")
    data object AddActivity : Route("add")
    data object Activities : Route("activities")
    data object Insights : Route("insights")
    data object Report : Route("report")
    data object Detail : Route("detail/{activityId}") {
        fun create(activityId: Long) = "detail/$activityId"
    }
}
