package dev.vinicius.busycardapp.domain.repository

interface IUserRepository<K, T>: IRepository<K, T> {
    suspend fun saveMyCardId(userId: K, cardId: K)

    suspend fun getMyCardsId(userId: K): List<K>

    suspend fun saveSharedCardId(userId: K, cardId: K)

    suspend fun removeSharedCardId(userId: K, cardId: K)

    suspend fun getSharedCardsId(userId: K): List<K>

    suspend fun saveDraftCardId(userId: K, cardId: K)

    suspend fun getDraftCardsId(userId: K): List<K>

    suspend fun removeDraftCardId(userId: K, cardId: K)

    suspend fun getUser(userId: K): T
}