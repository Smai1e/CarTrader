package dev.smai1e.carTrader.data.localCache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.smai1e.carTrader.data.localCache.USER_TABLE_NAME
import dev.smai1e.carTrader.data.localCache.models.UserDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    /**
     * Insert (or replace) user.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserDBO)

    /**
     * Insert (or replace) a list of users.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserDBO>)

    /**
     * Get auction that match the specified id.
     */
    @Query("SELECT * FROM $USER_TABLE_NAME WHERE id = :id")
    fun fetchUserById(id: Long): UserDBO

    /**
     * Get a list of bidders that match specified auction id.
     */
    @Query(
        """
            SELECT users.id, users.first_name, users.middle_name, users.last_name,
            users.photo_url, users.email, users.phone, users.money,
            users.bids_count, users.auctions_count, users.registration_date
            FROM users
            JOIN bids ON users.id = bids.bidder_id
            WHERE bids.auction_id = :auctionId
            GROUP BY users.id
        """
    )
    suspend fun fetchBiddersByAuctionId(auctionId: Long): List<UserDBO>

    /**
     * Get stream of bidders that match specified auction id.
     */
    @Query(
        """
            SELECT users.id, users.first_name, users.middle_name, users.last_name,
            users.photo_url, users.email, users.phone, users.money,
            users.bids_count, users.auctions_count, users.registration_date
            FROM users
            JOIN bids ON users.id = bids.bidder_id
            WHERE bids.auction_id = :auctionId
            GROUP BY users.id
        """
    )
    fun observeBiddersByAuctionId(auctionId: Long): Flow<List<UserDBO>>

    /**
     * Delete all entries from table.
     */
    @Query("DELETE FROM $USER_TABLE_NAME")
    suspend fun clear()
}