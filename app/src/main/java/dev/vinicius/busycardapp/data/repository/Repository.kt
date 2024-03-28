package dev.vinicius.busycardapp.data.repository

interface Repository<K, T> {

    suspend fun getById(id: K): T

    suspend fun save(item: T)
}