package dev.smai1e.carTrader.data.localCache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.smai1e.carTrader.data.localCache.AUCTION_TABLE_NAME
import dev.smai1e.carTrader.data.localCache.models.AuctionDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface AuctionDao {

    /**
     * Insert (or replace) auction.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuction(auction: AuctionDBO)

    /**
     * Insert (or replace) a list of auctions.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuctions(auctions: List<AuctionDBO>)

    /**
     * Get auction that match the specified id.
     */
    @Query("SELECT * FROM $AUCTION_TABLE_NAME WHERE id = :id")
    suspend fun fetchAuctionById(id: Long): AuctionDBO

    /**
     * Get a list of auctions that match the specified params.
     */
    @Query(
        """
            SELECT * FROM $AUCTION_TABLE_NAME 
            WHERE (:searchRequest IS NULL OR brand LIKE :searchRequest OR model LIKE :searchRequest)
            AND (:minBidStart IS NULL OR min_bid >= :minBidStart) AND (:minBidEnd IS NULL OR min_bid <= :minBidEnd)
            AND (:brand IS NULL OR brand = :brand) AND (:model IS NULL OR model = :model)
            AND (:manufacturerDateStart IS NULL OR manufacturer_date >= :manufacturerDateStart) AND (:manufacturerDateEnd IS NULL OR manufacturer_date <= :manufacturerDateEnd)
            AND (:color IS NULL OR color = :color)
            AND (:horsepowerStart IS NULL OR horsepower >= :horsepowerStart) AND (:horsepowerEnd IS NULL OR horsepower <= :horsepowerEnd)
            AND (:mileageStart IS NULL OR mileage >= :mileageStart) AND (:mileageEnd IS NULL OR mileage <= :mileageEnd)
            AND (:driveWheel IS NULL OR drive_wheel = :driveWheel)
            AND (:gearbox IS NULL OR gearbox = :gearbox)
        """
    )
    suspend fun fetchAllAuctions(
        searchRequest: String?,
        minBidStart: Int?,
        minBidEnd: Int?,
        brand: String?,
        model: String?,
        manufacturerDateStart: Int?,
        manufacturerDateEnd: Int?,
        color: String?,
        horsepowerStart: Int?,
        horsepowerEnd: Int?,
        mileageStart: Int?,
        mileageEnd: Int?,
        driveWheel: String?,
        gearbox: String?
    ): List<AuctionDBO>

    /**
     * Get stream of auctions that match the specified params.
     */
    @Query(
        """
            SELECT * FROM $AUCTION_TABLE_NAME 
            WHERE (:searchRequest IS NULL OR brand LIKE :searchRequest OR model LIKE :searchRequest)
            AND (:minBidStart IS NULL OR min_bid >= :minBidStart) AND (:minBidEnd IS NULL OR min_bid <= :minBidEnd)
            AND (:brand IS NULL OR brand = :brand) AND (:model IS NULL OR model = :model)
            AND (:manufacturerDateStart IS NULL OR manufacturer_date >= :manufacturerDateStart) AND (:manufacturerDateEnd IS NULL OR manufacturer_date <= :manufacturerDateEnd)
            AND (:color IS NULL OR color = :color)
            AND (:horsepowerStart IS NULL OR horsepower >= :horsepowerStart) AND (:horsepowerEnd IS NULL OR horsepower <= :horsepowerEnd)
            AND (:mileageStart IS NULL OR mileage >= :mileageStart) AND (:mileageEnd IS NULL OR mileage <= :mileageEnd)
            AND (:driveWheel IS NULL OR drive_wheel = :driveWheel)
            AND (:gearbox IS NULL OR gearbox = :gearbox)
        """
    )
    fun observeAllAuctions(
        searchRequest: String?,
        minBidStart: Int?,
        minBidEnd: Int?,
        brand: String?,
        model: String?,
        manufacturerDateStart: Int?,
        manufacturerDateEnd: Int?,
        color: String?,
        horsepowerStart: Int?,
        horsepowerEnd: Int?,
        mileageStart: Int?,
        mileageEnd: Int?,
        driveWheel: String?,
        gearbox: String?
    ): Flow<List<AuctionDBO>>

    /**
     * Delete all entries from table.
     */
    @Query("DELETE FROM $AUCTION_TABLE_NAME")
    suspend fun clear()
}