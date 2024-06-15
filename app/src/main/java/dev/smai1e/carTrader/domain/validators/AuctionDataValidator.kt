package dev.smai1e.carTrader.domain.validators

import dev.smai1e.carTrader.data.RequestResult
import dev.smai1e.carTrader.data.network.models.request.AuctionRequest
import dev.smai1e.carTrader.data.network.models.request.CarRequest
import dev.smai1e.carTrader.domain.errorTypes.ValidationError
import javax.inject.Inject

/**
 * The validator of the auction data.
 * - If successful, it returns:
 *      - RequestResult.Success<Unit>.
 * - In case of error:
 *      - RequestResult.Error<ValidationError.AuctionDataError>
 */
class AuctionDataValidator @Inject constructor() {

    fun validate(auction: AuctionRequest): RequestResult<Unit, ValidationError.AuctionDataError> {
        val result = validateCar(auction.car)
        if (result is RequestResult.Error) return result

        if (auction.openDate.isNullOrBlank()) return RequestResult.Error(ValidationError.AuctionDataError.INCORRECT_OPEN_DATE)
        if (auction.closeDate.isNullOrBlank()) return RequestResult.Error(ValidationError.AuctionDataError.INCORRECT_CLOSE_DATE)
        if (auction.minBid == null) return RequestResult.Error(ValidationError.AuctionDataError.EMPTY_MIN_BID)
        if (auction.car.description.isNullOrBlank()) return RequestResult.Error(ValidationError.AuctionDataError.EMPTY_DESCRIPTION)
        return RequestResult.Success(Unit)
    }

    private fun validateCar(car: CarRequest): RequestResult<Unit, ValidationError.AuctionDataError> {
        if (car.brand.isNullOrBlank()) return RequestResult.Error(ValidationError.AuctionDataError.EMPTY_BRAND)
        if (car.model.isNullOrBlank()) return RequestResult.Error(ValidationError.AuctionDataError.EMPTY_MODEL)
        if (car.manufacturerDate == null) return RequestResult.Error(ValidationError.AuctionDataError.INCORRECT_MANUFACTURER_DATE)
        if (car.mileage == null) return RequestResult.Error(ValidationError.AuctionDataError.EMPTY_MILEAGE)
        if (car.gearbox == null) return RequestResult.Error(ValidationError.AuctionDataError.EMPTY_GEARBOX)
        if (car.color == null) return RequestResult.Error(ValidationError.AuctionDataError.EMPTY_COLOR)
        if (car.driveWheel == null) return RequestResult.Error(ValidationError.AuctionDataError.EMPTY_DRIVE_WHEEL)
        if (car.horsepower == null) return RequestResult.Error(ValidationError.AuctionDataError.EMPTY_HORSEPOWER)

        val vin = car.vin
        if (vin == null || !vinRegex.matches(vin)) {
            return RequestResult.Error(ValidationError.AuctionDataError.INCORRECT_VIN)
        }
        return RequestResult.Success(Unit)
    }

    private companion object {
        val vinRegex = Regex("^[A-HJ-NPR-Z\\d]{17}\$", RegexOption.IGNORE_CASE)
    }
}