package dev.vinicius.busycardapp.data.repository

import kotlinx.coroutines.flow.Flow

interface Repository<K, T> {

    suspend fun getById(id: K): Flow<T>

    suspend fun save(item: T)
}