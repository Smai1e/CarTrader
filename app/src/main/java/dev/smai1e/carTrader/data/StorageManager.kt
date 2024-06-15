package dev.smai1e.carTrader.data

import android.os.Environment
import android.os.StatFs
import dev.smai1e.carTrader.data.localCache.CarTraderDatabase
import dev.smai1e.carTrader.data.localCache.dao.SystemDao
import dev.smai1e.carTrader.domain.models.Memory
import java.io.File
import javax.inject.Inject

/**
 * Allows to get information about the storage in phone
 * using [CarTraderDatabase] and [SystemDao] as data sources.
 */
class StorageManager @Inject constructor(
    private val database: CarTraderDatabase,
    private val systemDao: SystemDao
) {

    fun getAvailableSpace(): Memory {
        val path: File = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return Memory(availableBlocks * blockSize)
    }

    fun getTotalSpace(): Memory {
        val path: File = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return Memory(totalBlocks * blockSize)
    }

    suspend fun getCache(): Memory {
        val result = systemDao.getDatabaseSizeInfo().first()
        val pageSize = result.pageSize?.toLong() ?: 0L
        val pageCount = result.pageCount?.toLong() ?: 0L
        return Memory(pageSize * pageCount)
    }

    fun clearAllTables() {
        database.clearAllTables()
    }
}