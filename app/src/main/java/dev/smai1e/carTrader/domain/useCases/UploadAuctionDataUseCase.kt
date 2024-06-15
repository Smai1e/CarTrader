package dev.smai1e.carTrader.domain.useCases

import dev.smai1e.carTrader.data.network.models.request.AuctionRequest
import dev.smai1e.carTrader.di.DefaultDispatcher
import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.domain.validators.AuctionDataValidator
import dev.smai1e.carTrader.domain.errorTypes.RootError
import dev.smai1e.carTrader.domain.repositoryInterfaces.AuctionsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

/**
 * A use case that allows the user to create an auction
 * after checking his information for validity.
 */
class UploadAuctionDataUseCase @Inject constructor(
    private val auctionsRepository: AuctionsRepository,
    private val auctionValidator: AuctionDataValidator,
    private val externalScope: CoroutineScope,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(
        uris: List<String>,
        data: AuctionRequest
    ): RequestResult<Unit, RootError> {

        val validationResult = auctionValidator.validate(data)
        if (validationResult is RequestResult.Error) {
            return validationResult
        }

        val requestResult = withContext(externalScope.coroutineContext) {
            val parts = getMultipartData(uris)
            when (val response = auctionsRepository.uploadImages(parts)) {
                is RequestResult.Loading -> RequestResult.Loading
                is RequestResult.Success -> {
                    data.let {
                        it.car.imageUrlList = response.data
                        auctionsRepository.createAuction(it)
                    }
                }
                is RequestResult.Error -> RequestResult.Error(response.error)
            }
        }

        return requestResult
    }

    private fun getMultipartData(uris: List<String>): List<MultipartBody.Part> {
        return uris.map { uri ->
            val file = File(uri)
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("files", file.name, requestBody)
        }
    }
}