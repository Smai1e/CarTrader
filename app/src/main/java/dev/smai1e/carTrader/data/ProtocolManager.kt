package dev.smai1e.carTrader.data

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import dev.smai1e.carTrader.di.IoDispatcher
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.domain.repositoryInterfaces.FileName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProtocolManager @Inject constructor(
    private val applicationContext: Context,
    private val externalScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    /**
     * Saves the file to the specified directory.
     */
    suspend fun saveProtocolToDirectory(
        directoryUri: Uri,
        auctionId: Long,
        data: ByteArray
    ): RequestResult<FileName, DataError> {

        return withContext(externalScope.coroutineContext + ioDispatcher) {
            val documentFile = DocumentFile.fromTreeUri(applicationContext, directoryUri)
            val file = documentFile?.createFile("application/pdf", "auction_protocol_$auctionId")
            val fileName = file?.let { outputFile ->
                applicationContext.contentResolver.openOutputStream(outputFile.uri)
                    ?.use { outputStream ->
                        outputStream.write(data)
                    }
                outputFile.name
            }
            if (fileName != null) {
                RequestResult.Success(fileName)
            } else {
                RequestResult.Error(DataError.LocalStorageError.NOT_BE_SAVED)
            }
        }
    }
}