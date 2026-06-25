package com.devlens.domain.usecases

import com.devlens.domain.models.DeveloperActivity
import com.devlens.domain.models.ManagerReport
import com.devlens.domain.models.MisalignmentCategory

class GenerateManagerReportUseCase {
    operator fun invoke(activities: List<DeveloperActivity>): ManagerReport {
        val sevenDaysMillis = 7L * 24L * 60L * 60L * 1000L
        val cutoff = System.currentTimeMillis() - sevenDaysMillis
        val weekActivities = activities
            .filter { it.activityDate >= cutoff }
            .sortedByDescending { it.activityDate }
        val highImpact = weekActivities.filter { it.analysis.effectiveScore >= 75 }.take(5)
        val invisible = weekActivities
            .filter { it.analysis.category == MisalignmentCategory.InvisibleHighValue }
            .take(5)
        val averageEffective = weekActivities.map { it.analysis.effectiveScore }.averageOrZero()
        val averagePerceived = weekActivities.map { it.analysis.perceivedScore }.averageOrZero()

        val suggestions = buildList {
            if (invisible.isNotEmpty()) add("Dar visibilidade a atividades tecnicas de alto impacto com evidencias antes/depois.")
            if (averageEffective > averagePerceived + 12) add("Criar um ritual semanal para traduzir impacto tecnico em linguagem de produto.")
            add("Relacionar atividades relevantes a OKRs, riscos mitigados ou metricas de experiencia.")
            add("Manter backlog tecnico priorizado e compartilhado com lideranca.")
        }.distinct()

        val summary = if (weekActivities.isEmpty()) {
            "Nenhuma atividade registrada nesta semana."
        } else {
            "Na semana analisada foram registradas ${weekActivities.size} atividades. " +
                "A media de valor efetivo foi ${averageEffective.toInt()} e a media de valor percebido foi ${averagePerceived.toInt()}. " +
                "Foram encontradas ${invisible.size} atividades de alto valor com baixa visibilidade gerencial."
        }

        val shareable = buildString {
            appendLine("Resumo semanal DevLens")
            appendLine(summary)
            if (highImpact.isNotEmpty()) {
                appendLine()
                appendLine("Principais impactos:")
                highImpact.forEach { activity ->
                    appendLine("- ${activity.title}: valor efetivo ${activity.analysis.effectiveScore}, percepcao ${activity.analysis.perceivedScore}.")
                }
            }
            if (suggestions.isNotEmpty()) {
                appendLine()
                appendLine("Proximas acoes:")
                suggestions.forEach { appendLine("- $it") }
            }
        }.trim()

        return ManagerReport(
            weeklySummary = summary,
            highImpactActivities = highImpact,
            invisibleActivities = invisible,
            alignmentSuggestions = suggestions,
            shareableSummary = shareable
        )
    }

    private fun List<Int>.averageOrZero(): Double = if (isEmpty()) 0.0 else average()
}
