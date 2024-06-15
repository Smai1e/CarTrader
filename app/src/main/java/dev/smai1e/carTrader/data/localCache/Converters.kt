package dev.smai1e.carTrader.data.localCache

import androidx.room.TypeConverter
import dev.smai1e.carTrader.data.toColor
import dev.smai1e.carTrader.data.toDriveWheel
import dev.smai1e.carTrader.data.toGearbox
import dev.smai1e.carTrader.domain.models.Color
import dev.smai1e.carTrader.domain.models.DriveWheel
import dev.smai1e.carTrader.domain.models.Gearbox

/**
 * Database (Room) type converter for URL
 * - from:
 * ```
 *      listOf("aaa", "bbb", "ccc")
 * ```
 * - to:
 * ```
 *      "aaa bbb ccc"
 * ```
 * and vice versa
 */
class UrlListConverter {

    @TypeConverter
    fun toUrlList(value: String): List<String> {
        return value.split(" ")
    }

    @TypeConverter
    fun fromUrlLIst(list: List<String>): String {
        return list.joinToString(" ")
    }
}

/**
 * Database (Room) type converter for Enum Color class
 * - from:
 * ```
 *      Color.BLACK
 * ```
 * - to:
 * ```
 *      "BLACK"
 * ```
 * and vice versa
 */
class ColorConverter {

    @TypeConverter
    fun fromColor(color: Color): String {
        return color.name
    }

    @TypeConverter
    fun toColor(value: String): Color {
        return value.toColor()
    }
}

/**
 * Database (Room) type converter for Enum DriveWheel class
 * - from:
 * ```
 *      DriveWheel.AWD
 * ```
 * - to:
 * ```
 *      "AWD"
 * ```
 * and vice versa
 */
class DriveWheelConverter {

    @TypeConverter
    fun fromDriveWheel(driveWheel: DriveWheel): String {
        return driveWheel.name
    }

    @TypeConverter
    fun toDriveWheel(value: String): DriveWheel {
        return value.toDriveWheel()
    }
}

/**
 * Database (Room) type converter for Enum Gearbox class
 * - from:
 * ```
 *      Gearbox.Manual
 * ```
 * - to:
 * ```
 *      "Manual"
 * ```
 * and vice versa
 */
class GearboxConverter {

    @TypeConverter
    fun fromGearbox(gearbox: Gearbox): String {
        return gearbox.name
    }

    @TypeConverter
    fun toGearbox(value: String): Gearbox {
        return value.toGearbox()
    }
}