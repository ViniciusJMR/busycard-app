package dev.vinicius.busycardapp.domain.usecase.card

import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.data.repository.Repository
import dev.vinicius.busycardapp.domain.model.card.Card
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveCard @Inject constructor(
    private val repository: Repository<Long, Card>
): UseCase.NoSource<Card>() {
    override suspend fun execute(param: Card) = flow {
        emit(repository.save(param))
    }
}