package dev.smai1e.carTrader.data.repository

import android.content.Context
import dev.smai1e.carTrader.domain.repositoryInterfaces.TokenRepository
import javax.inject.Inject

/**
 * Implementation a [TokenRepository].
 */
class TokenRepositoryImpl @Inject constructor(
    private val context: Context
) : TokenRepository {

    private val preferences =
        context.applicationContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    /**
     * Saves authentication token to [preferences].
     */
    override fun saveAuthToken(authToken: String) {
        val editor = preferences.edit()
        editor.putString(AUTH_TOKEN, authToken)
        editor.apply()
    }

    /**
     * Fetches authentication token from [preferences].
     */
    override fun fetchAuthToken(): String? =
        if (preferences.contains(AUTH_TOKEN)) {
            preferences.getString(AUTH_TOKEN, null)
        } else {
            null
        }

    /**
     * Deletes authentication token from [preferences].
     */
    override fun clearAuthToken() {
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }

    private companion object {
        private const val PREFERENCES_NAME = "CarTraderPref"
        private const val AUTH_TOKEN = "AuthToken"
    }
}