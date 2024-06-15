package dev.smai1e.carTrader.domain.models

/**
 * Entity for operations in the domain layer.
 */
data class StorageSpaceState(
    val occupiedSpace: Memory,
    val totalSpace: Memory,
    val cache: Memory
)