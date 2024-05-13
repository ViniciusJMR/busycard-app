package dev.vinicius.busycardapp.domain.usecase.card

import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.domain.repository.IRepository
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.model.card.CardState
import dev.vinicius.busycardapp.domain.usecase.auth.GetUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCardById @Inject constructor(
    private val getSharedCards: GetSharedCards,
    private val getMyCards: GetMyCards,
    private val getUserUseCase: GetUserUseCase,
    private val repository: IRepository<String, Card>
): UseCase<String, Card>() {
    override suspend fun execute(param: String): Flow<Card> = flow {
        repository.getById(param)
            .collect { card ->
                getUserUseCase()
                    .collect{ user ->
                        val myCards = user.myCards
                        val sharedCards = user.sharedCards
                        card.cardState = if (myCards.any { it== param }) CardState.MINE else CardState.NOT_SHARED
                        card.cardState = if (sharedCards.any { it == param }) CardState.SHARED else CardState.NOT_SHARED
                    }
                emit(card)
            }
    }
}