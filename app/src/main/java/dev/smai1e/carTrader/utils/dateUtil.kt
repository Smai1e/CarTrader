package dev.smai1e.carTrader.utils

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Convert "2024-03-09T23:04:15Z" into "09.03.2024 23:04" (format depends on the localization)
 */
fun String.convertISO8601ToFormattedDate(): String {
    val zonedDateTime = ZonedDateTime.parse(this)
    val duration = Duration.ofSeconds(45678765L).toHours()
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
    return zonedDateTime.format(dateFormatter)
}
/**
 * Convert 1710025455000L into "2024-03-09T23:04:15Z"
 */
fun Long.convertEpochTimeMillisToISO8601(): String {
    val instant = Instant.ofEpochMilli(this)
    val zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
    return zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}
/**
 * Convert 34567898765L into "09.03.2024 23:04" (format depends on the localization)
 */
fun Long.convertEpochTimeMillisToFormattedDate(): String {
    val instant = Instant.ofEpochMilli(this)
    val zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
    return zonedDateTime.format(dateFormatter)
}
/**
 * Convert "2024-03-09T23:04:15Z" into {2024-03-09T23:04:15Z}
 */
fun String.parseToZonedDateTime(): ZonedDateTime {
    return ZonedDateTime.parse(this)
}

/**
 * Gets the number of seconds in current minute in this duration.
 * Value from 0 to 59
 */
fun Duration.second() = this.seconds % 60

/**
 * Gets the number of minutes in current hour in this duration.
 * Value from 0 to 59
 */
fun Duration.minute() = this.seconds / 60 % 60

/**
 * Gets the number of hours in current day in this duration.
 * Value from 0 to 23
 */
fun Duration.hour() = this.seconds / 60 / 60 % 24

/**
 * Gets the number of days in this duration.
 */
fun Duration.day() = this.seconds / 60 / 60 / 24