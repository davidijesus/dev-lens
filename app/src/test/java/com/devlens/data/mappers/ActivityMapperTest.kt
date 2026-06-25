package com.devlens.data.mappers

import com.devlens.domain.models.ActivityType
import com.devlens.domain.models.AiAnalysis
import com.devlens.domain.models.DeveloperActivity
import com.devlens.domain.models.MisalignmentCategory
import org.junit.Assert.assertEquals
import org.junit.Test

class ActivityMapperTest {
    @Test
    fun mapsDomainToEntityAndBack() {
        val domain = DeveloperActivity(
            id = 7,
            title = "Revisao de codigo",
            description = "Revisei PR critico com risco de bug em producao.",
            minutesSpent = 50,
            type = ActivityType.CodeReview,
            tags = listOf("review", "qualidade"),
            selfPerceivedVisibility = 40,
            activityDate = 100L,
            createdAt = 90L,
            analysis = AiAnalysis(
                effectiveScore = 78,
                perceivedScore = 44,
                gap = 34,
                category = MisalignmentCategory.InvisibleHighValue,
                confidence = 0.82f,
                explanation = "Explicacao rastreavel",
                effectiveFactors = listOf("qualidade", "risco"),
                perceivedFactors = listOf("baixa comunicacao"),
                recommendations = listOf("Gerar resumo"),
                analyzedAt = 110L
            )
        )

        val mapped = domain.toEntity().toDomain()

        assertEquals(domain, mapped)
    }
}
