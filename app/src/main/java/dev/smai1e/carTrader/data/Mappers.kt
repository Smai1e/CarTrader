package dev.smai1e.carTrader.data

import dev.smai1e.carTrader.data.localCache.models.AuctionDBO
import dev.smai1e.carTrader.data.localCache.models.BidDBO
import dev.smai1e.carTrader.data.localCache.models.CarDBO
import dev.smai1e.carTrader.data.localCache.models.UserDBO
import dev.smai1e.carTrader.data.network.models.request.SignInRequest
import dev.smai1e.carTrader.data.network.models.request.SignUpRequest
import dev.smai1e.carTrader.data.network.models.response.AuctionResponse
import dev.smai1e.carTrader.data.network.models.response.BidResponse
import dev.smai1e.carTrader.data.network.models.response.BrandResponse
import dev.smai1e.carTrader.data.network.models.response.CarResponse
import dev.smai1e.carTrader.data.network.models.response.ModelResponse
import dev.smai1e.carTrader.data.network.models.response.UserInfoResponse
import dev.smai1e.carTrader.domain.errorTypes.DataError
import dev.smai1e.carTrader.domain.models.Auction
import dev.smai1e.carTrader.domain.models.Bid
import dev.smai1e.carTrader.domain.models.BidWithUser
import dev.smai1e.carTrader.domain.models.Brand
import dev.smai1e.carTrader.domain.models.Car
import dev.smai1e.carTrader.domain.models.Color
import dev.smai1e.carTrader.domain.models.DriveWheel
import dev.smai1e.carTrader.domain.models.Gearbox
import dev.smai1e.carTrader.domain.models.Model
import dev.smai1e.carTrader.domain.models.SignInData
import dev.smai1e.carTrader.domain.models.SignUpData
import dev.smai1e.carTrader.domain.models.Status
import dev.smai1e.carTrader.domain.models.UserInfo
import dev.smai1e.carTrader.utils.parseToZonedDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

typealias ErrorCode = Int

fun ErrorCode.toNetworkError(): DataError.NetworkError {
    return when (this) {
        400 -> DataError.NetworkError.BAD_REQUEST
        401 -> DataError.NetworkError.UNAUTHORIZED
        404 -> DataError.NetworkError.NOT_FOUND
        408 -> DataError.NetworkError.REQUEST_TIMEOUT
        409 -> DataError.NetworkError.CONFLICT
        // ...
        else -> DataError.NetworkError.UNKNOWN
    }
}

fun String.toColor(): Color {
    return enumValueOf<Color>(this)
}

fun String.toDriveWheel(): DriveWheel {
    return enumValueOf<DriveWheel>(this)
}

fun String.toGearbox(): Gearbox {
    return enumValueOf<Gearbox>(this)
}

fun String.toStatus(): Status {
    return enumValueOf<Status>(this)
}

fun AuctionResponse.toAuctionDBO() = AuctionDBO(
    id = this.id,
    sellerId = this.sellerId,
    openDate = this.openDate,
    closeDate = this.closeDate,
    minBid = this.minBid,
    lot = this.car.toCarDBO()
)

fun AuctionDBO.toAuction() = Auction(
    id = this.id,
    sellerId = this.sellerId,
    openDate = this.openDate,
    closeDate = this.closeDate,
    minBid = this.minBid,
    auctionStatus = getStatus(this.openDate, this.closeDate),
    car = this.lot.toCar()
)

fun CarDBO.toCar() = Car(
    id = this.cardId,
    imageUrlList = this.imageUrlList,
    brand = this.brand,
    model = this.model,
    manufacturerDate = this.manufacturerDate,
    color = this.color,
    mileage = this.mileage,
    horsepower = this.horsepower,
    driveWheel = this.driveWheel,
    gearbox = this.gearbox,
    description = this.description,
    vin = this.vin
)

fun getStatus(openDateISO8601: String, closeDateISO8601: String): Status {
    val currentDate = ZonedDateTime.now(ZoneId.systemDefault())
    return when {
        currentDate.isBefore(openDateISO8601.parseToZonedDateTime()) ->
            Status.WAITING

        currentDate.isAfter(closeDateISO8601.parseToZonedDateTime()) ->
            Status.COMPLETED

        else -> Status.ACTIVE
    }
}

fun CarResponse.toCarDBO() = CarDBO(
    cardId = this.id,
    imageUrlList = this.imageUrlList,
    brand = this.brand,
    model = this.model,
    manufacturerDate = manufacturerDate,
    color = this.color.toColor(),
    mileage = this.mileage,
    horsepower = this.horsepower,
    driveWheel = this.driveWheel.toDriveWheel(),
    gearbox = this.gearbox.toGearbox(),
    description = this.description,
    vin = this.vin
)

fun CarResponse.toCar() = Car(
    id = this.id,
    imageUrlList = this.imageUrlList,
    brand = this.brand,
    model = this.model,
    manufacturerDate = manufacturerDate,
    color = this.color.toColor(),
    mileage = this.mileage,
    horsepower = this.horsepower,
    driveWheel = this.driveWheel.toDriveWheel(),
    gearbox = this.gearbox.toGearbox(),
    description = this.description,
    vin = this.vin
)

fun ModelResponse.toModel() = Model(
    name = this.name
)

fun BrandResponse.toBrand() = Brand(
    name = this.name,
    logoUrl = this.logoUrl
)

fun AuctionResponse.toAuction() = Auction(
    id = this.id,
    sellerId = this.sellerId,
    openDate = this.openDate,
    closeDate = this.closeDate,
    minBid = this.minBid,
    auctionStatus = this.auctionStatus.toStatus(),
    car = this.car.toCar()
)


fun BidResponse.toBid() = Bid(
    id = this.id,
    auctionId = this.auctionId,
    bidderId = this.bidderId,
    bidTime = this.bidTime,
    bidAmount = this.bidAmount
)

fun BidDBO.toBid() = Bid(
    id = this.id,
    auctionId = this.auctionId,
    bidderId = this.bidderId,
    bidTime = this.bidTime,
    bidAmount = this.bidAmount
)

fun BidResponse.toBidDBO() = BidDBO(
    id = this.id,
    auctionId = this.auctionId,
    bidderId = this.bidderId,
    bidTime = this.bidTime,
    bidAmount = this.bidAmount
)

fun Bid.toBidWithUser(users: List<UserInfo>) = BidWithUser(
    id = this.id,
    auctionId = this.auctionId,
    bidder = users.findLast { it.id == this.bidderId }!!,
    bidTime = this.bidTime,
    bidAmount = this.bidAmount
)

fun UserInfoResponse.toUser() = UserInfo(
    id = this.id,
    firstName = this.firstName,
    middleName = this.middleName,
    lastName = this.lastName,
    photoUrl = this.photoUrl,
    email = this.email,
    phone = this.phone,
    money = this.money,
    bidsCount = this.bidsCount,
    auctionsCount = this.auctionsCount,
    registrationDate = this.registrationDate
)

fun UserDBO.toUser() = UserInfo(
    id = this.id,
    firstName = this.firstName,
    middleName = this.middleName,
    lastName = this.lastName,
    photoUrl = this.photoUrl,
    email = this.email,
    phone = this.phone,
    money = this.money,
    bidsCount = this.bidsCount,
    auctionsCount = this.auctionsCount,
    registrationDate = this.registrationDate
)

fun UserInfoResponse.toUserDBO() = UserDBO(
    id = this.id,
    firstName = this.firstName,
    middleName = this.middleName,
    lastName = this.lastName,
    photoUrl = this.photoUrl,
    email = this.email,
    phone = this.phone,
    money = this.money,
    bidsCount = this.bidsCount,
    auctionsCount = this.auctionsCount,
    registrationDate = this.registrationDate
)

fun SignUpData.toSignUpRequest() = SignUpRequest(
    email = this.email,
    firstName = this.firstName,
    middleName = this.middleName,
    lastName = this.lastName,
    phone = this.phone,
    password = this.password
)

fun SignInData.toSignInRequest() = SignInRequest(
    email = this.email,
    password = this.password
)