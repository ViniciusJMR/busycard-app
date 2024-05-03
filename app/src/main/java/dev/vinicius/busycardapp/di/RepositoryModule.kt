package dev.vinicius.busycardapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.vinicius.busycardapp.data.repository.impl.AuthRepository
import dev.vinicius.busycardapp.data.repository.impl.BucketRepository
import dev.vinicius.busycardapp.domain.repository.IRepository
import dev.vinicius.busycardapp.data.repository.impl.CardRepository
import dev.vinicius.busycardapp.data.repository.impl.UserRepository
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.model.user.User
import dev.vinicius.busycardapp.domain.repository.Auth
import dev.vinicius.busycardapp.domain.repository.Bucket
import dev.vinicius.busycardapp.domain.repository.IUserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCardRepository(
        cardRepository: CardRepository
    ): IRepository<String, Card>

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepository: UserRepository
    ): IUserRepository<String, User>

    @Binds
    @Singleton
    abstract fun bindBucketRepository(
        bucket: BucketRepository
    ): Bucket

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        auth: AuthRepository
    ): Auth
}