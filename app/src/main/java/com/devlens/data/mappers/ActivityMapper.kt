package com.devlens.data.mappers

import com.devlens.data.entities.ActivityEntity
import com.devlens.domain.models.ActivityType
import com.devlens.domain.models.AiAnalysis
import com.devlens.domain.models.DeveloperActivity
import com.devlens.domain.models.MisalignmentCategory

private const val Separator = "||"

fun DeveloperActivity.toEntity(): ActivityEntity = ActivityEntity(
    id = id,
    title = title,
    description = description,
    minutesSpent = minutesSpent,
    type = type.name,
    tags = tags.joinToString(Separator),
    selfPerceivedVisibility = selfPerceivedVisibility,
    activityDate = activityDate,
    createdAt = createdAt,
    effectiveScore = analysis.effectiveScore,
    perceivedScore = analysis.perceivedScore,
    gap = analysis.gap,
    category = analysis.category.name,
    confidence = analysis.confidence,
    explanation = analysis.explanation,
    effectiveFactors = analysis.effectiveFactors.joinToString(Separator),
    perceivedFactors = analysis.perceivedFactors.joinToString(Separator),
    recommendations = analysis.recommendations.joinToString(Separator),
    analyzedAt = analysis.analyzedAt
)

fun ActivityEntity.toDomain(): DeveloperActivity = DeveloperActivity(
    id = id,
    title = title,
    description = description,
    minutesSpent = minutesSpent,
    type = enumValueOfOrDefault(type, ActivityType.Other),
    tags = tags.splitList(),
    selfPerceivedVisibility = selfPerceivedVisibility,
    activityDate = activityDate,
    createdAt = createdAt,
    analysis = AiAnalysis(
        effectiveScore = effectiveScore,
        perceivedScore = perceivedScore,
        gap = gap,
        category = enumValueOfOrDefault(category, MisalignmentCategory.NeedsAttention),
        confidence = confidence,
        explanation = explanation,
        effectiveFactors = effectiveFactors.splitList(),
        perceivedFactors = perceivedFactors.splitList(),
        recommendations = recommendations.splitList(),
        analyzedAt = analyzedAt
    )
)

private fun String.splitList(): List<String> =
    if (isBlank()) emptyList() else split(Separator).filter { it.isNotBlank() }

private inline fun <reified T : Enum<T>> enumValueOfOrDefault(value: String, default: T): T =
    runCatching { enumValueOf<T>(value) }.getOrDefault(default)
