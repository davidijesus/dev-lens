package com.devlens.domain.usecases

import com.devlens.domain.models.DashboardMetrics
import com.devlens.domain.models.DeveloperActivity
import com.devlens.domain.models.MisalignmentCategory

class GetDashboardUseCase {
    operator fun invoke(activities: List<DeveloperActivity>): DashboardMetrics {
        if (activities.isEmpty()) return DashboardMetrics()

        val averageEffective = activities.map { it.analysis.effectiveScore }.average().toInt()
        val averagePerceived = activities.map { it.analysis.perceivedScore }.average().toInt()
        val distribution = activities.groupingBy { it.analysis.category }.eachCount()

        return DashboardMetrics(
            totalActivities = activities.size,
            averageEffective = averageEffective,
            averagePerceived = averagePerceived,
            invisibleHighValueCount = distribution[MisalignmentCategory.InvisibleHighValue] ?: 0,
            overestimatedCount = distribution[MisalignmentCategory.OverestimatedLowValue] ?: 0,
            criticalActivities = activities
                .sortedWith(
                    compareByDescending<DeveloperActivity> { kotlin.math.abs(it.analysis.gap) }
                        .thenByDescending { it.analysis.effectiveScore }
                )
                .take(5),
            categoryDistribution = distribution
        )
    }
}
