package dev.smai1e.carTrader.domain.useCases

import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.data.StorageManager
import dev.smai1e.carTrader.di.IoDispatcher
import dev.smai1e.carTrader.domain.errorTypes.DataError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A use case that allows to clear the cache.
 */
class ClearCacheUseCase @Inject constructor(
    private val storageManager: StorageManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(): RequestResult<Unit, DataError> {
        return withContext(ioDispatcher) {
            try {
                storageManager.clearAllTables()
                RequestResult.Success(Unit)
            } catch (e: Exception) {
                RequestResult.Error(DataError.LocalStorageException(e))
            }
        }
    }
}