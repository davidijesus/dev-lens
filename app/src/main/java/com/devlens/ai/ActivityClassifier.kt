package com.devlens.ai

import com.devlens.domain.models.ActivityInput
import com.devlens.domain.models.AiAnalysis

interface ActivityClassifier {
    fun classify(input: ActivityInput): AiAnalysis
}
