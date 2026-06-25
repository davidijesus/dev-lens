package com.devlens.ai

import com.devlens.domain.models.ActivityInput
import com.devlens.domain.models.ActivityType
import com.devlens.domain.models.AiAnalysis
import com.devlens.domain.models.MisalignmentCategory
import kotlin.math.abs
import kotlin.math.roundToInt

class LocalAiEngine(
    private val explanationGenerator: ExplanationGenerator = ExplanationGenerator(),
    private val recommendationEngine: RecommendationEngine = RecommendationEngine()
) : ActivityClassifier {

    override fun classify(input: ActivityInput): AiAnalysis {
        val text = buildString {
            append(input.title.lowercase())
            append(" ")
            append(input.description.lowercase())
            append(" ")
            append(input.tags.joinToString(" ").lowercase())
        }

        val effectiveFactors = mutableListOf<String>()
        val perceivedFactors = mutableListOf<String>()

        var effective = 35 + typeEffectiveWeight(input.type)
        var perceived = 25 + typePerceivedWeight(input.type) + input.selfPerceivedVisibility / 2

        effective += scoreKeywords(
            text,
            impactKeywords,
            12,
            "termos de impacto em produto, cliente ou negocio",
            effectiveFactors
        )
        effective += scoreKeywords(
            text,
            technicalQualityKeywords,
            10,
            "sinais de qualidade tecnica, confiabilidade ou divida tecnica",
            effectiveFactors
        )
        effective += scoreKeywords(
            text,
            urgencyKeywords,
            8,
            "sinais de urgencia, incidente ou criticidade",
            effectiveFactors
        )

        perceived += scoreKeywords(
            text,
            visibilityKeywords,
            14,
            "sinais de entrega visivel, comunicacao ou apresentacao",
            perceivedFactors
        )
        perceived += scoreKeywords(
            text,
            managementKeywords,
            10,
            "vinculo com OKR, meta, roadmap ou stakeholder",
            perceivedFactors
        )

        if (input.minutesSpent >= 180) {
            effective += 8
            effectiveFactors += "tempo significativo investido (${input.minutesSpent} min)"
        }
        if (input.minutesSpent in 15..45 && input.type == ActivityType.TechnicalMeeting) {
            perceived += 6
            perceivedFactors += "atividade curta e facil de reportar"
        }
        if (input.tags.isEmpty()) {
            perceived -= 8
            perceivedFactors += "ausencia de tags reduz rastreabilidade gerencial"
        }
        if (visibilityKeywords.none { text.contains(it) } && input.type in invisibleValueTypes) {
            perceived -= 10
            perceivedFactors += "trabalho tecnico importante sem evidencia de comunicacao formal"
        }

        val effectiveScore = effective.coerceIn(0, 100)
        val perceivedScore = perceived.coerceIn(0, 100)
        val gap = effectiveScore - perceivedScore
        val category = categorize(effectiveScore, perceivedScore, gap)
        val confidence = confidence(effectiveFactors.size, perceivedFactors.size, gap)
        val recommendations = recommendationEngine.recommend(
            effectiveScore = effectiveScore,
            perceivedScore = perceivedScore,
            category = category,
            factors = effectiveFactors + perceivedFactors
        )

        return AiAnalysis(
            effectiveScore = effectiveScore,
            perceivedScore = perceivedScore,
            gap = gap,
            category = category,
            confidence = confidence,
            explanation = explanationGenerator.explain(
                input = input,
                effectiveScore = effectiveScore,
                perceivedScore = perceivedScore,
                category = category,
                effectiveFactors = effectiveFactors,
                perceivedFactors = perceivedFactors
            ),
            effectiveFactors = effectiveFactors.ifEmpty { listOf("tipo da atividade sugere impacto moderado") },
            perceivedFactors = perceivedFactors.ifEmpty { listOf("baixa presenca de sinais explicitos de visibilidade") },
            recommendations = recommendations,
            analyzedAt = System.currentTimeMillis()
        )
    }

    private fun typeEffectiveWeight(type: ActivityType): Int = when (type) {
        ActivityType.Feature -> 18
        ActivityType.Bugfix -> 20
        ActivityType.Refactoring -> 22
        ActivityType.Support -> 14
        ActivityType.TechnicalMeeting -> 8
        ActivityType.CodeReview -> 16
        ActivityType.Performance -> 28
        ActivityType.TechnicalDebt -> 24
        ActivityType.Documentation -> 12
        ActivityType.Other -> 8
    }

    private fun typePerceivedWeight(type: ActivityType): Int = when (type) {
        ActivityType.Feature -> 24
        ActivityType.Bugfix -> 18
        ActivityType.Refactoring -> 6
        ActivityType.Support -> 8
        ActivityType.TechnicalMeeting -> 18
        ActivityType.CodeReview -> 8
        ActivityType.Performance -> 10
        ActivityType.TechnicalDebt -> 4
        ActivityType.Documentation -> 14
        ActivityType.Other -> 8
    }

    private fun scoreKeywords(
        text: String,
        keywords: Set<String>,
        points: Int,
        factor: String,
        factors: MutableList<String>
    ): Int {
        val hits = keywords.count { text.contains(it) }
        if (hits > 0) {
            factors += "$factor ($hits ocorrencia(s))"
        }
        return (hits * points).coerceAtMost(points * 2)
    }

    private fun categorize(effective: Int, perceived: Int, gap: Int): MisalignmentCategory = when {
        abs(gap) <= 14 -> MisalignmentCategory.Aligned
        effective >= 70 && gap >= 18 -> MisalignmentCategory.InvisibleHighValue
        perceived >= 65 && gap <= -18 -> MisalignmentCategory.OverestimatedLowValue
        else -> MisalignmentCategory.NeedsAttention
    }

    private fun confidence(effectiveSignals: Int, perceivedSignals: Int, gap: Int): Float {
        val signalScore = ((effectiveSignals + perceivedSignals) * 0.08f).coerceAtMost(0.32f)
        val gapScore = (abs(gap) / 100f * 0.28f).coerceAtMost(0.28f)
        return ((0.52f + signalScore + gapScore).coerceIn(0.55f, 0.96f) * 100).roundToInt() / 100f
    }

    companion object {
        private val impactKeywords = setOf(
            "cliente", "usuario", "usuarios", "receita", "conversao", "dashboard",
            "produto", "negocio", "meta", "okr", "sla", "incidente", "producao"
        )
        private val technicalQualityKeywords = setOf(
            "refator", "legado", "teste", "cobertura", "arquitetura", "performance",
            "query", "latencia", "cache", "seguranca", "divida", "observabilidade",
            "confiabilidade", "deploy", "bug", "erro"
        )
        private val urgencyKeywords = setOf(
            "critico", "critica", "urgente", "bloqueio", "falha", "queda", "hotfix",
            "rollback", "p0", "p1"
        )
        private val visibilityKeywords = setOf(
            "release", "demo", "apresentacao", "daily", "review", "stakeholder",
            "entrega", "roadmap", "relatorio", "comunicado", "metricas", "documentado"
        )
        private val managementKeywords = setOf(
            "okr", "kpi", "meta", "gestor", "lideranca", "produto", "prioridade",
            "planejamento", "sprint", "backlog"
        )
        private val invisibleValueTypes = setOf(
            ActivityType.Refactoring,
            ActivityType.Performance,
            ActivityType.TechnicalDebt,
            ActivityType.CodeReview,
            ActivityType.Support
        )
    }
}
