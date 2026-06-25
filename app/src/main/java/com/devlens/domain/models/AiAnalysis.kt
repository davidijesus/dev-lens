package com.devlens.domain.models

data class AiAnalysis(
    val effectiveScore: Int,
    val perceivedScore: Int,
    val gap: Int,
    val category: MisalignmentCategory,
    val confidence: Float,
    val explanation: String,
    val effectiveFactors: List<String>,
    val perceivedFactors: List<String>,
    val recommendations: List<String>,
    val analyzedAt: Long
)
