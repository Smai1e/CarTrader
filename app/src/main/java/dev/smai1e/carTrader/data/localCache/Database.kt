package dev.smai1e.carTrader.data.localCache

import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.smai1e.carTrader.data.localCache.dao.AuctionDao
import dev.smai1e.carTrader.data.localCache.dao.BidDao
import dev.smai1e.carTrader.data.localCache.dao.SystemDao
import dev.smai1e.carTrader.data.localCache.dao.UserDao
import dev.smai1e.carTrader.data.localCache.models.AuctionDBO
import dev.smai1e.carTrader.data.localCache.models.BidDBO
import dev.smai1e.carTrader.data.localCache.models.UserDBO

@androidx.room.Database(
    entities = [
        AuctionDBO::class,
        UserDBO::class,
        BidDBO::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    ColorConverter::class,
    DriveWheelConverter::class,
    GearboxConverter::class,
    UrlListConverter::class
)
abstract class CarTraderDatabase : RoomDatabase() {
    abstract val systemDao: SystemDao
    abstract val auctionDao: AuctionDao
    abstract val userDao: UserDao
    abstract val bidDao: BidDao
}

const val DATABASE_NAME = "CarTrader.db"
const val AUCTION_TABLE_NAME = "auctions"
const val USER_TABLE_NAME = "users"
const val BID_TABLE_NAME = "bids"