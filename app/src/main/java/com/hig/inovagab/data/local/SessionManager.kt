package com.hig.inovagab.data.local
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class SessionManager(private val context: Context) {
    companion object {
        private val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
    }
    val jwtToken: Flow<String?> = context.dataStore.data.map{it[JWT_TOKEN_KEY]}
    val userRole: Flow<String?> = context.dataStore.data.map{it[USER_ROLE_KEY]}

    suspend fun saveSession(token: String, role: String) {
        context.dataStore.edit { prefs ->
            prefs[JWT_TOKEN_KEY] = token
            prefs[USER_ROLE_KEY] = role
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}