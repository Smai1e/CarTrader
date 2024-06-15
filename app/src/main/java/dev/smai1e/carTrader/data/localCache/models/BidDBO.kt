package dev.smai1e.carTrader.data.localCache.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.smai1e.carTrader.data.localCache.BID_TABLE_NAME

/**
 * Database (Room) entity for storing bids in the
 * local storage.
 */
@Entity(tableName = BID_TABLE_NAME)
data class BidDBO (

    @PrimaryKey
    val id: Long,

    @ColumnInfo("auction_id")
    val auctionId: Long,

    @ColumnInfo("bidder_id")
    val bidderId: Long,

    @ColumnInfo("bid_time")
    val bidTime: String,

    @ColumnInfo("bid_amount")
    val bidAmount: Int
)