package com.bloom.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bloom.app.data.model.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "bloom_prefs")

class PreferencesManager(private val context: Context) {
    
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_KEY = stringPreferencesKey("user_data")
    }
    
    private val gson = Gson()
    
    // Token
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }
    
    fun getToken(): String? = runBlocking {
        context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.first()
    }
    
    val tokenFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }
    
    // User
    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_KEY] = gson.toJson(user)
        }
    }
    
    fun getUser(): User? = runBlocking {
        context.dataStore.data.map { preferences ->
            preferences[USER_KEY]?.let { json ->
                gson.fromJson(json, User::class.java)
            }
        }.first()
    }
    
    val userFlow: Flow<User?> = context.dataStore.data.map { preferences ->
        preferences[USER_KEY]?.let { json ->
            gson.fromJson(json, User::class.java)
        }
    }
    
    // Clear all
    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    // Check if logged in
    fun isLoggedIn(): Boolean {
        return !getToken().isNullOrEmpty()
    }
}