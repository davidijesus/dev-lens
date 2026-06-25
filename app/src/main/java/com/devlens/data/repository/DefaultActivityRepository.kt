package com.devlens.data.repository

import com.devlens.ai.ActivityClassifier
import com.devlens.data.dao.ActivityDao
import com.devlens.data.mappers.toDomain
import com.devlens.data.mappers.toEntity
import com.devlens.domain.models.ActivityInput
import com.devlens.domain.models.ActivityType
import com.devlens.domain.models.DeveloperActivity
import com.devlens.domain.repositories.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class DefaultActivityRepository(
    private val dao: ActivityDao,
    private val classifier: ActivityClassifier
) : ActivityRepository {
    override fun observeActivities(): Flow<List<DeveloperActivity>> =
        dao.observeActivities().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getActivities(): List<DeveloperActivity> =
        dao.getActivities().map { it.toDomain() }

    override suspend fun findActivity(id: Long): DeveloperActivity? =
        dao.findActivity(id)?.toDomain()

    override suspend fun saveActivity(activity: DeveloperActivity): Long =
        dao.insertActivity(activity.toEntity())

    override suspend fun seedIfEmpty() {
        if (dao.countActivities() > 0) return

        sampleInputs().forEach { input ->
            saveActivity(
                DeveloperActivity(
                    title = input.title,
                    description = input.description,
                    minutesSpent = input.minutesSpent,
                    type = input.type,
                    tags = input.tags,
                    selfPerceivedVisibility = input.selfPerceivedVisibility,
                    activityDate = input.activityDate,
                    createdAt = System.currentTimeMillis(),
                    analysis = classifier.classify(input)
                )
            )
        }
    }

    private fun sampleInputs(): List<ActivityInput> {
        val calendar = Calendar.getInstance()
        fun daysAgo(days: Int): Long {
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.DAY_OF_YEAR, -days)
            return calendar.timeInMillis
        }

        return listOf(
            ActivityInput(
                title = "Otimizar query critica do dashboard",
                description = "Reduzi latencia de query em producao que afetava usuarios do dashboard executivo.",
                minutesSpent = 210,
                type = ActivityType.Performance,
                tags = listOf("performance", "dashboard", "producao"),
                selfPerceivedVisibility = 38,
                activityDate = daysAgo(1)
            ),
            ActivityInput(
                title = "Refatorar modulo legado de pagamentos",
                description = "Removi duplicacao, aumentei cobertura de testes e reduzi divida tecnica antes da proxima feature.",
                minutesSpent = 180,
                type = ActivityType.Refactoring,
                tags = listOf("arquitetura", "testes", "divida-tecnica"),
                selfPerceivedVisibility = 30,
                activityDate = daysAgo(2)
            ),
            ActivityInput(
                title = "Demo da nova tela de onboarding",
                description = "Apresentei entrega da sprint para produto, gestor e stakeholders no review.",
                minutesSpent = 70,
                type = ActivityType.Feature,
                tags = listOf("release", "review", "stakeholder"),
                selfPerceivedVisibility = 84,
                activityDate = daysAgo(3)
            ),
            ActivityInput(
                title = "Suporte a incidente de autenticacao",
                description = "Investiguei falha critica p1, ajudei rollback e documentei causa raiz para evitar recorrencia.",
                minutesSpent = 150,
                type = ActivityType.Support,
                tags = listOf("incidente", "p1", "confiabilidade"),
                selfPerceivedVisibility = 48,
                activityDate = daysAgo(4)
            ),
            ActivityInput(
                title = "Reuniao de alinhamento de backlog",
                description = "Discussao de prioridade do roadmap com produto e lideranca para planejamento da sprint.",
                minutesSpent = 45,
                type = ActivityType.TechnicalMeeting,
                tags = listOf("roadmap", "planejamento"),
                selfPerceivedVisibility = 78,
                activityDate = daysAgo(5)
            )
        )
    }
}
