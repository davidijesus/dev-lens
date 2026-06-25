package com.devlens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.devlens.presentation.navigation.DevLensNavHost
import com.devlens.presentation.theme.DevLensTheme
import com.devlens.presentation.viewmodels.DevLensViewModel
import com.devlens.worker.InsightWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleInsightWorker()

        setContent {
            DevLensRoot((application as DevLensApplication).container)
        }
    }

    private fun scheduleInsightWorker() {
        val request = PeriodicWorkRequestBuilder<InsightWorker>(12, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "devlens-insight-refresh",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}

@Composable
private fun DevLensRoot(container: DevLensContainer) {
    val viewModel: DevLensViewModel = viewModel(factory = DevLensViewModel.Factory(container))
    val onboardingComplete by viewModel.onboardingComplete.collectAsStateWithLifecycle()

    DevLensTheme {
        DevLensNavHost(
            viewModel = viewModel,
            onboardingComplete = onboardingComplete
        )
    }
}
