package com.devlens

import android.content.Context
import androidx.room.Room
import com.devlens.ai.LocalAiEngine
import com.devlens.data.local.DevLensDatabase
import com.devlens.data.local.OnboardingPreferences
import com.devlens.data.repository.DefaultActivityRepository
import com.devlens.domain.usecases.AnalyzeActivityUseCase
import com.devlens.domain.usecases.GenerateManagerReportUseCase
import com.devlens.domain.usecases.GetDashboardUseCase
import com.devlens.domain.usecases.SaveActivityUseCase

class DevLensContainer(context: Context) {
    val aiEngine = LocalAiEngine()

    private val database = Room.databaseBuilder(
        context,
        DevLensDatabase::class.java,
        "devlens.db"
    ).build()

    val repository = DefaultActivityRepository(database.activityDao(), aiEngine)
    val preferences = OnboardingPreferences(context)
    val analyzeActivity = AnalyzeActivityUseCase(aiEngine)
    val saveActivity = SaveActivityUseCase(repository, analyzeActivity)
    val getDashboard = GetDashboardUseCase()
    val generateReport = GenerateManagerReportUseCase()
}
