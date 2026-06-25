package com.devlens.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.devlens.DevLensContainer
import com.devlens.domain.models.ActivityInput
import com.devlens.domain.models.ActivityType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ActivityFormState(
    val title: String = "",
    val description: String = "",
    val minutesSpent: String = "60",
    val type: ActivityType = ActivityType.Feature,
    val tags: String = "",
    val activityDate: String = defaultDateText(),
    val selfPerceivedVisibility: Float = 50f,
    val isSaving: Boolean = false,
    val error: String? = null
)

class DevLensViewModel(
    private val container: DevLensContainer
) : ViewModel() {
    val activities = container.repository.observeActivities()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val dashboard = activities.map(container.getDashboard::invoke)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), container.getDashboard(emptyList()))

    val report = activities.map(container.generateReport::invoke)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), container.generateReport(emptyList()))

    val onboardingComplete = container.preferences.isOnboardingComplete
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    private val _formState = MutableStateFlow(ActivityFormState())
    val formState: StateFlow<ActivityFormState> = _formState

    init {
        viewModelScope.launch {
            container.repository.seedIfEmpty()
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch { container.preferences.setOnboardingComplete() }
    }

    fun updateTitle(value: String) = _formState.update { it.copy(title = value, error = null) }
    fun updateDescription(value: String) = _formState.update { it.copy(description = value, error = null) }
    fun updateMinutes(value: String) = _formState.update { it.copy(minutesSpent = value.filter(Char::isDigit), error = null) }
    fun updateType(value: ActivityType) = _formState.update { it.copy(type = value, error = null) }
    fun updateTags(value: String) = _formState.update { it.copy(tags = value, error = null) }
    fun updateActivityDate(value: String) = _formState.update { it.copy(activityDate = value, error = null) }
    fun updateVisibility(value: Float) = _formState.update { it.copy(selfPerceivedVisibility = value, error = null) }

    fun saveActivity(onSaved: (Long) -> Unit) {
        val state = _formState.value
        val minutes = state.minutesSpent.toIntOrNull()
        val activityDate = parseDateText(state.activityDate)
        if (state.title.isBlank() || state.description.isBlank() || minutes == null || minutes <= 0 || activityDate == null) {
            _formState.update { it.copy(error = "Informe titulo, descricao, tempo e data no formato dd/MM/aaaa.") }
            return
        }

        viewModelScope.launch {
            _formState.update { it.copy(isSaving = true, error = null) }
            val id = container.saveActivity(
                ActivityInput(
                    title = state.title.trim(),
                    description = state.description.trim(),
                    minutesSpent = minutes,
                    type = state.type,
                    tags = state.tags.split(",").map { it.trim() }.filter { it.isNotBlank() },
                    selfPerceivedVisibility = state.selfPerceivedVisibility.toInt(),
                    activityDate = activityDate
                )
            )
            _formState.value = ActivityFormState()
            onSaved(id)
        }
    }

    class Factory(private val container: DevLensContainer) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DevLensViewModel(container) as T
        }
    }
}

private fun defaultDateText(): String =
    SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date())

private fun parseDateText(value: String): Long? {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).apply {
        isLenient = false
    }
    return runCatching { formatter.parse(value)?.time }.getOrNull()
}
