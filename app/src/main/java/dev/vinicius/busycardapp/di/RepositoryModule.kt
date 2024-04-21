package dev.vinicius.busycardapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.vinicius.busycardapp.data.repository.impl.BucketRepository
import dev.vinicius.busycardapp.domain.repository.Repository
import dev.vinicius.busycardapp.data.repository.impl.CardRepository
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.repository.Bucket
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCardRepository(
        cardRepository: CardRepository
    ): Repository<String, Card>

    @Binds
    @Singleton
    abstract fun bindBucketRepository(
        bucket: BucketRepository
    ): Bucket

}