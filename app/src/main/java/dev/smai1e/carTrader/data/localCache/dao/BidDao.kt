package dev.smai1e.carTrader.data.localCache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.smai1e.carTrader.data.localCache.BID_TABLE_NAME
import dev.smai1e.carTrader.data.localCache.models.BidDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface BidDao {

    /**
     * Insert (or replace) bid.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBid(bid: BidDBO)

    /**
     * Insert (or replace) a list of bids.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBids(bids: List<BidDBO>)

    /**
     * Get a list of bids that match the specified auction id.
     */
    @Query("SELECT * FROM $BID_TABLE_NAME WHERE auction_id = :id")
    suspend fun fetchBidsByAuctionId(id: Long): List<BidDBO>

    /**
     * Get stream of bids that match the specified auction id.
     */
    @Query("SELECT * FROM $BID_TABLE_NAME WHERE auction_id = :id")
    fun observeBidsByAuctionId(id: Long): Flow<List<BidDBO>>

    /**
     * Delete all entries from table.
     */
    @Query("DELETE FROM $BID_TABLE_NAME")
    suspend fun clear()
}