package com.devlens.ai

import com.devlens.domain.models.ActivityInput
import com.devlens.domain.models.ActivityType
import com.devlens.domain.models.MisalignmentCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocalAiEngineTest {
    private val engine = LocalAiEngine()

    @Test
    fun calculatesHighEffectiveValueForCriticalPerformanceWork() {
        val result = engine.classify(
            ActivityInput(
                title = "Otimizar query critica do dashboard",
                description = "Reduzi latencia em producao para usuarios e evitei incidente de SLA.",
                minutesSpent = 210,
                type = ActivityType.Performance,
                tags = listOf("performance", "dashboard"),
                selfPerceivedVisibility = 30,
                activityDate = 1L
            )
        )

        assertTrue(result.effectiveScore >= 80)
    }

    @Test
    fun calculatesHigherPerceivedValueWhenCommunicationSignalsExist() {
        val result = engine.classify(
            ActivityInput(
                title = "Demo de entrega da sprint",
                description = "Apresentacao em review para gestor, produto e stakeholders com metricas de KPI.",
                minutesSpent = 60,
                type = ActivityType.Feature,
                tags = listOf("release", "review", "okr"),
                selfPerceivedVisibility = 90,
                activityDate = 1L
            )
        )

        assertTrue(result.perceivedScore >= 75)
    }

    @Test
    fun identifiesInvisibleHighValueMisalignment() {
        val result = engine.classify(
            ActivityInput(
                title = "Refatorar modulo legado critico",
                description = "Removi divida tecnica, corrigi bug critico e aumentei cobertura de teste.",
                minutesSpent = 240,
                type = ActivityType.Refactoring,
                tags = listOf("arquitetura", "testes"),
                selfPerceivedVisibility = 20,
                activityDate = 1L
            )
        )

        assertEquals(MisalignmentCategory.InvisibleHighValue, result.category)
        assertTrue(result.gap > 0)
    }

    @Test
    fun generatesExplanationWithTraceableFactors() {
        val result = engine.classify(
            ActivityInput(
                title = "Suporte a incidente p1",
                description = "Investigacao de falha critica em producao e rollback para restaurar usuarios.",
                minutesSpent = 120,
                type = ActivityType.Support,
                tags = listOf("incidente", "p1"),
                selfPerceivedVisibility = 45,
                activityDate = 1L
            )
        )

        assertTrue(result.explanation.contains("valor efetivo", ignoreCase = true))
        assertTrue(result.effectiveFactors.isNotEmpty())
        assertTrue(result.perceivedFactors.isNotEmpty())
    }

    @Test
    fun generatesRecommendationsForLowVisibilityWork() {
        val result = engine.classify(
            ActivityInput(
                title = "Melhorar observabilidade do servico",
                description = "Adicionei metricas e logs para confiabilidade e incidentes futuros.",
                minutesSpent = 180,
                type = ActivityType.TechnicalDebt,
                tags = emptyList(),
                selfPerceivedVisibility = 15,
                activityDate = 1L
            )
        )

        assertTrue(result.recommendations.any { it.contains("gestor", ignoreCase = true) || it.contains("relatorio", ignoreCase = true) })
    }
}
