package com.devlens.domain.usecases

import com.devlens.domain.models.ActivityInput
import com.devlens.domain.models.DeveloperActivity
import com.devlens.domain.repositories.ActivityRepository

class SaveActivityUseCase(
    private val repository: ActivityRepository,
    private val analyzeActivity: AnalyzeActivityUseCase
) {
    suspend operator fun invoke(input: ActivityInput): Long {
        val analysis = analyzeActivity(input)
        return repository.saveActivity(
            DeveloperActivity(
                title = input.title,
                description = input.description,
                minutesSpent = input.minutesSpent,
                type = input.type,
                tags = input.tags,
                selfPerceivedVisibility = input.selfPerceivedVisibility,
                activityDate = input.activityDate,
                createdAt = System.currentTimeMillis(),
                analysis = analysis
            )
        )
    }
}
