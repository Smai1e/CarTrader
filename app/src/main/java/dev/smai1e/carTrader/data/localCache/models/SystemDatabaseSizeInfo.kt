package dev.smai1e.carTrader.data.localCache.models

import androidx.room.ColumnInfo

/**
 * Entity for storing system info from the local storage.
 */
data class SystemDatabaseSizeInfo(
    @ColumnInfo("page_size")
    var pageSize: Int? = null,
    @ColumnInfo("page_count")
    var pageCount: Int? = null
)