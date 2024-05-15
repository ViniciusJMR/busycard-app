package dev.vinicius.busycardapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface IRepository<K, T> {

    suspend fun getAll(): Flow<List<T>>

    suspend fun getById(id: K): Flow<T>

    suspend fun getByIds(ids: List<K>): Flow<List<T>>

    suspend fun save(item: T): K

    suspend fun deleteById(id: K)
}