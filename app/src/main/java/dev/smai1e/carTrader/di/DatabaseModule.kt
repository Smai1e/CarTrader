package dev.smai1e.carTrader.di

import android.content.Context
import androidx.room.Room
import dev.smai1e.carTrader.data.localCache.CarTraderDatabase
import dev.smai1e.carTrader.data.localCache.DATABASE_NAME
import dev.smai1e.carTrader.data.localCache.dao.AuctionDao
import dev.smai1e.carTrader.data.localCache.dao.BidDao
import dev.smai1e.carTrader.data.localCache.dao.SystemDao
import dev.smai1e.carTrader.data.localCache.dao.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): CarTraderDatabase {
        return Room.databaseBuilder(
            checkNotNull(context.applicationContext),
            CarTraderDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideSystemDao(database: CarTraderDatabase): SystemDao {
        return database.systemDao
    }

    @Provides
    fun provideAuctionDao(database: CarTraderDatabase): AuctionDao {
        return database.auctionDao
    }

    @Provides
    fun provideUserDao(database: CarTraderDatabase): UserDao {
        return database.userDao
    }

    @Provides
    fun provideBidDao(database: CarTraderDatabase): BidDao {
        return database.bidDao
    }
}