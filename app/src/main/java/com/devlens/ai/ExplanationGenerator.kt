package com.devlens.ai

import com.devlens.domain.models.ActivityInput
import com.devlens.domain.models.MisalignmentCategory

class ExplanationGenerator {
    fun explain(
        input: ActivityInput,
        effectiveScore: Int,
        perceivedScore: Int,
        category: MisalignmentCategory,
        effectiveFactors: List<String>,
        perceivedFactors: List<String>
    ): String {
        val effectiveReason = effectiveFactors.take(3).joinToString("; ").ifBlank {
            "impacto tecnico moderado identificado pelo tipo da atividade"
        }
        val perceivedReason = perceivedFactors.take(3).joinToString("; ").ifBlank {
            "poucos sinais de comunicacao, entrega visivel ou vinculo com metas"
        }

        return when (category) {
            MisalignmentCategory.InvisibleHighValue ->
                "A atividade '${input.title}' recebeu alto valor efetivo ($effectiveScore) por causa de $effectiveReason. " +
                    "O valor percebido ficou menor ($perceivedScore) porque $perceivedReason. A acao recomendada e tornar o impacto mensuravel e visivel para a gestao."

            MisalignmentCategory.OverestimatedLowValue ->
                "A atividade '${input.title}' parece mais visivel ($perceivedScore) do que efetivamente impactante ($effectiveScore). " +
                    "Os sinais de percepcao vieram de $perceivedReason, mas o impacto tecnico encontrado foi limitado. Vale revisar prioridade e evidencia de resultado."

            MisalignmentCategory.NeedsAttention ->
                "A atividade '${input.title}' apresenta desalinhamento relevante entre impacto ($effectiveScore) e percepcao ($perceivedScore). " +
                    "Os principais sinais foram $effectiveReason e $perceivedReason. O registro deve ser enriquecido com contexto, evidencias e proximo passo claro."

            MisalignmentCategory.Aligned ->
                "A atividade '${input.title}' esta relativamente alinhada: valor efetivo $effectiveScore e valor percebido $perceivedScore. " +
                    "A classificacao combina $effectiveReason com $perceivedReason, indicando boa coerencia entre trabalho realizado e visibilidade."
        }
    }
}
