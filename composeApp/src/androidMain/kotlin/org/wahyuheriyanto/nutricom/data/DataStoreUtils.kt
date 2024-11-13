package org.wahyuheriyanto.nutricom.data

// Create a file DataStoreUtils.kt
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

object DataStoreUtils {
    private val EMAIL_KEY = stringPreferencesKey("email")
    private val PASSWORD_KEY = stringPreferencesKey("password")
    private val UID_KEY = stringPreferencesKey("uid")


    // Save email and password
    suspend fun saveCredentials(context: Context, email: String, password: String) {
        context.dataStore.edit { prefs ->
            prefs[EMAIL_KEY] = email
            prefs[PASSWORD_KEY] = password
        }
    }

    //Preparing

    suspend fun saveLoginCredentials(context: Context, uid: String) {
        context.dataStore.edit { prefs ->
            prefs[UID_KEY] = uid
        }
    }

    // Retrieve saved email and password
    fun getCredentials(context: Context): Flow<Pair<String?, String?>> {
        return context.dataStore.data.map { prefs ->
            Pair(prefs[EMAIL_KEY], prefs[PASSWORD_KEY])
        }
    }

    fun getLoginCredentials(context: Context): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[UID_KEY]
        }
    }
}
