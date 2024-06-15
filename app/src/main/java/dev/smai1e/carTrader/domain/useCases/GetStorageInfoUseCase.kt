package dev.smai1e.carTrader.domain.useCases

import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.data.StorageManager
import dev.smai1e.carTrader.di.IoDispatcher
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.domain.models.StorageSpaceState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A use case that allows to get information about storage in the phone.
 * - occupied space
 * - total space
 * - cache
 */
class GetStorageInfoUseCase @Inject constructor(
    private val storageManager: StorageManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(): RequestResult<StorageSpaceState, DataError> {
        return withContext(ioDispatcher) {
            try {
                val availableSpace = storageManager.getAvailableSpace()
                val totalSpace = storageManager.getTotalSpace()
                val cache = storageManager.getCache()
                StorageSpaceState(
                    totalSpace - availableSpace,
                    totalSpace,
                    cache
                ).let {
                    RequestResult.Success(it)
                }
            } catch (e: Exception) {
                RequestResult.Error(DataError.LocalStorageException(e))
            }
        }
    }
}