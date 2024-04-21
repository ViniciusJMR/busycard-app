package dev.vinicius.busycardapp.domain.usecase.card

import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.domain.repository.Repository
import dev.vinicius.busycardapp.domain.model.card.Card
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAll @Inject constructor(
    private val repository: Repository<String, Card>
): UseCase.NoParam<List<Card>>() {
    override suspend fun execute(): Flow<List<Card>> =
        repository.getAll()
}