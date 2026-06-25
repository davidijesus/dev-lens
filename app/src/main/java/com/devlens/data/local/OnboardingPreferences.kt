package com.devlens.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "devlens_preferences")

class OnboardingPreferences(private val context: Context) {
    val isOnboardingComplete: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[ONBOARDING_COMPLETE] ?: false
    }

    suspend fun setOnboardingComplete() {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETE] = true
        }
    }

    private companion object {
        val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
    }
}
