package com.devlens.domain.usecases

import com.devlens.ai.ActivityClassifier
import com.devlens.domain.models.ActivityInput
import com.devlens.domain.models.AiAnalysis

class AnalyzeActivityUseCase(
    private val classifier: ActivityClassifier
) {
    operator fun invoke(input: ActivityInput): AiAnalysis = classifier.classify(input)
}
