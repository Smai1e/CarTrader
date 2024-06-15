package dev.smai1e.carTrader.data.localCache.dao

import androidx.room.Dao
import androidx.room.Query
import dev.smai1e.carTrader.data.localCache.AUCTION_TABLE_NAME
import dev.smai1e.carTrader.data.localCache.BID_TABLE_NAME
import dev.smai1e.carTrader.data.localCache.USER_TABLE_NAME
import dev.smai1e.carTrader.data.localCache.models.SystemDatabaseSizeInfo

@Dao
interface SystemDao {

    /**
     * Get info about database size.
     */
    @Query(
        """
        SELECT s.*, c.* 
        FROM pragma_page_size as s 
        JOIN pragma_page_count as c
        """
    )
    suspend fun getDatabaseSizeInfo(): List<SystemDatabaseSizeInfo>

    /**
     * Delete all entries from auctions table.
     */
    @Query("DELETE FROM $AUCTION_TABLE_NAME")
    suspend fun clearAuctionTable()

    /**
     * Delete all entries from bids table.
     */
    @Query("DELETE FROM $BID_TABLE_NAME")
    suspend fun clearBidTable()

    /**
     * Delete all entries from users table.
     */
    @Query("DELETE FROM $USER_TABLE_NAME")
    suspend fun clearUserTable()
}