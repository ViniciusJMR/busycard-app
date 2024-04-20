package dev.vinicius.busycardapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface Repository<K, T> {

    suspend fun getAll(): Flow<List<T>>

    suspend fun getById(id: K): Flow<T>

    suspend fun save(item: T)
}