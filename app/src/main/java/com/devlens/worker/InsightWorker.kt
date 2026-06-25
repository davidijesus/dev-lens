package com.devlens.worker

import android.content.Context
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.devlens.ai.LocalAiEngine
import com.devlens.data.local.DevLensDatabase
import com.devlens.data.repository.DefaultActivityRepository

class InsightWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val database = Room.databaseBuilder(
            applicationContext,
            DevLensDatabase::class.java,
            "devlens.db"
        ).build()

        return try {
            val repository = DefaultActivityRepository(database.activityDao(), LocalAiEngine())
            repository.seedIfEmpty()
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        } finally {
            database.close()
        }
    }
}
