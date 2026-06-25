package com.devlens.ai

import com.devlens.domain.models.MisalignmentCategory

class RecommendationEngine {
    fun recommend(
        effectiveScore: Int,
        perceivedScore: Int,
        category: MisalignmentCategory,
        factors: List<String>
    ): List<String> {
        val recommendations = linkedSetOf<String>()

        if (category == MisalignmentCategory.InvisibleHighValue) {
            recommendations += "Gerar resumo para o gestor com impacto, evidencia e risco reduzido."
            recommendations += "Relacionar esta atividade a um OKR ou metrica de produto."
            recommendations += "Registrar antes/depois tecnico para tornar o valor observavel."
        }

        if (category == MisalignmentCategory.OverestimatedLowValue) {
            recommendations += "Revalidar prioridade com o time antes de ampliar o investimento."
            recommendations += "Conectar a atividade a um resultado mensuravel ou reduzir escopo."
        }

        if (effectiveScore >= 75) {
            recommendations += "Adicionar evidencia de impacto, como tempo economizado, incidentes evitados ou usuarios afetados."
        }

        if (perceivedScore < 55) {
            recommendations += "Comunicar em daily, review ou relatorio semanal com linguagem de negocio."
            recommendations += "Transformar em item de backlog tecnico visivel para acompanhamento."
        }

        if (factors.any { it.contains("performance", ignoreCase = true) }) {
            recommendations += "Anexar comparativo de performance antes/depois."
        }

        if (recommendations.isEmpty()) {
            recommendations += "Manter registro da atividade e revisar se existe evidencia objetiva de impacto."
        }

        return recommendations.toList()
    }
}
