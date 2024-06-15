package dev.smai1e.carTrader.domain.repositoryInterfaces

interface TokenRepository {

    /**
     * Saves authentication token to local memory.
     */
    fun saveAuthToken(authToken: String)

    /**
     * Fetches authentication token.
     */
    fun fetchAuthToken(): String?

    /**
     * Deletes authentication token from local memory.
     */
    fun clearAuthToken()
}