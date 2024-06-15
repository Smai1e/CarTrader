package dev.smai1e.carTrader.data.localCache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.smai1e.carTrader.data.localCache.USER_TABLE_NAME

/**
 * Database (Room) entity for storing users in the
 * local storage.
 */
@Entity(tableName = USER_TABLE_NAME)
data class UserDBO(

    @PrimaryKey
    val id: Long,

    @ColumnInfo("first_name")
    val firstName: String,

    @ColumnInfo("middle_name")
    val middleName: String,

    @ColumnInfo("last_name")
    val lastName: String,

    @ColumnInfo("photo_url")
    val photoUrl: String,

    @ColumnInfo("email")
    val email: String,

    @ColumnInfo("phone")
    val phone: String,

    @ColumnInfo("money")
    val money: Int = 0,

    @ColumnInfo("bids_count")
    val bidsCount: Int,

    @ColumnInfo("auctions_count")
    val auctionsCount: Int,

    @ColumnInfo("registration_date")
    val registrationDate: String
)