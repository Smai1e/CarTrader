package dev.smai1e.carTrader.domain.models

import kotlin.math.log10
import kotlin.math.pow

enum class CapacityUnit {
    B,
    KB,
    MB,
    GB,
    TB
}

data class Memory(val byteCount: Long = 0L) {

    var value: Float = 0f
        private set
    var measure: CapacityUnit = CapacityUnit.B
        private set

    init {
        formatSpace(byteCount)
    }

    private fun formatSpace(size: Long) {
        val units = CapacityUnit.entries
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        value = size / 1024.0.pow(digitGroups.toDouble()).toFloat()
        measure = units[digitGroups]
    }

    override fun toString(): String {
        return String.format("%.1f ", value).plus(measure)
    }

    operator fun minus(other: Memory): Memory {
        return Memory(this.byteCount - other.byteCount)
    }

    operator fun plus(other: Memory): Memory {
        return Memory(this.byteCount + other.byteCount)
    }
}