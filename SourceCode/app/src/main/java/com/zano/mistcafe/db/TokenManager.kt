package com.zano.mistcafe.db

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val TIMESTAMP_KEY = longPreferencesKey("login_time")
    }

    val userId: Flow<Int?> = context.dataStore.data.map { preferences ->
        val savedId = preferences[USER_ID_KEY] ?: return@map null
        val savedTime = preferences[TIMESTAMP_KEY] ?: 0L

        val currentTime = System.currentTimeMillis()
        val twentyFourHoursInMillis = 24 * 60 * 60 * 1000 // 24 Hours

        // 2. Check if time has passed
        if (savedId != null && (currentTime - savedTime < twentyFourHoursInMillis)) {
            savedId // Still valid
        } else {
            null // Expired
        }
    }

    //save ID
    suspend fun saveUserId(id: Int?) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = id as Int
            preferences[TIMESTAMP_KEY] = System.currentTimeMillis() // Save Current Time
        }
    }


    // Delete the ID (Logout)
    suspend fun clearUserId() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
        }
    }
}