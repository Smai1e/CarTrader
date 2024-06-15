package dev.smai1e.carTrader.ui.models

import dev.smai1e.carTrader.domain.models.Auction
import dev.smai1e.carTrader.domain.models.Bid
import dev.smai1e.carTrader.domain.models.Car
import dev.smai1e.carTrader.domain.models.Color
import dev.smai1e.carTrader.domain.models.DriveWheel
import dev.smai1e.carTrader.domain.models.Gearbox
import dev.smai1e.carTrader.domain.models.Status
import dev.smai1e.carTrader.domain.models.UserInfo

fun ColorUI.toColor(): Color {
    return enumValueOf<Color>(this.name)
}

fun DriveWheelUI.toDriveWheel(): DriveWheel {
    return enumValueOf<DriveWheel>(this.name)
}

fun GearboxUI.toGearbox(): Gearbox {
    return enumValueOf<Gearbox>(this.name)
}

fun Color.toColorUI(): ColorUI {
    return enumValueOf<ColorUI>(this.name)
}

fun DriveWheel.toDriveWheelUI(): DriveWheelUI {
    return enumValueOf<DriveWheelUI>(this.name)
}

fun Gearbox.toGearboxUI(): GearboxUI {
    return enumValueOf<GearboxUI>(this.name)
}

fun Status.toStatusUI(): StatusUI {
    return enumValueOf<StatusUI>(this.name)
}

fun Auction.toAuctionUI() = AuctionUI(
    id = this.id,
    sellerId = this.sellerId,
    openDate = this.openDate,
    closeDate = this.closeDate,
    minBid = this.minBid,
    auctionStatus = this.auctionStatus.toStatusUI(),
    car = this.car.toCarUI()
)

fun Bid.toBidUI() = BidUI(
    id = this.id,
    auctionId = this.auctionId,
    bidderId = this.bidderId,
    bidTime = this.bidTime,
    bidAmount = this.bidAmount
)

fun Car.toCarUI() = CarUI(
    id = this.id,
    imageUrlList = this.imageUrlList,
    brand = this.brand,
    model = this.model,
    manufacturerDate = this.manufacturerDate,
    color = this.color.toColorUI(),
    mileage = this.mileage,
    horsepower = this.horsepower,
    driveWheel = this.driveWheel.toDriveWheelUI(),
    gearbox = this.gearbox.toGearboxUI(),
    description = this.description,
    vin = this.vin.uppercase()
)

fun UserInfo.toUserInfoUI() = UserInfoUI(
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