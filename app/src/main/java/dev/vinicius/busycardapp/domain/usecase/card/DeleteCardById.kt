package dev.vinicius.busycardapp.domain.usecase.card

import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.repository.IRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteCardById @Inject constructor(
    private val cardRepository: IRepository<String, Card>
): UseCase.NoSource<String>(){
    override suspend fun execute(param: String): Flow<Unit> = flow {
        cardRepository.deleteById(param)
        emit(Unit)
    }
}