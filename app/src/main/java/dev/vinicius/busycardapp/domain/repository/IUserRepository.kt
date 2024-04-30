package dev.vinicius.busycardapp.domain.repository

interface IUserRepository<K, T>: IRepository<K, T> {
    fun saveMyCardId(userId: String, cardId: String)
}