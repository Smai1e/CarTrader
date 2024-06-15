package dev.smai1e.carTrader.data.localCache.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.smai1e.carTrader.data.localCache.AUCTION_TABLE_NAME

/**
 * Database (Room) entity for storing auctions in the
 * local storage.
 */
@Entity(tableName = AUCTION_TABLE_NAME)
data class AuctionDBO(

    @PrimaryKey
    val id: Long,

    @ColumnInfo("seller_id")
    val sellerId: Long,

    @ColumnInfo("open_date")
    val openDate: String,

    @ColumnInfo("close_date")
    val closeDate: String,

    @ColumnInfo("min_bid")
    val minBid: Int,

    @Embedded
    val lot: CarDBO
)