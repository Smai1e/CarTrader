package dev.smai1e.carTrader.data.localCache.models

import androidx.room.ColumnInfo
import dev.smai1e.carTrader.domain.models.Color
import dev.smai1e.carTrader.domain.models.DriveWheel
import dev.smai1e.carTrader.domain.models.Gearbox

/**
 * Entity for storing auctions in the
 * local storage as a POJO.
 */
data class CarDBO (

    @ColumnInfo("card_id")
    val cardId: Long,

    @ColumnInfo("image_url_list")
    val imageUrlList: List<String>,

    @ColumnInfo("brand")
    val brand: String,

    @ColumnInfo("model")
    val model: String,

    @ColumnInfo("manufacturer_date")
    val manufacturerDate: Int,

    @ColumnInfo("color")
    val color: Color,

    @ColumnInfo("mileage")
    val mileage: Int,

    @ColumnInfo("horsepower")
    val horsepower: Int,

    @ColumnInfo("drive_wheel")
    val driveWheel: DriveWheel,

    @ColumnInfo("gearbox")
    val gearbox: Gearbox,

    @ColumnInfo("description")
    val description: String,

    @ColumnInfo("vin")
    val vin: String
)