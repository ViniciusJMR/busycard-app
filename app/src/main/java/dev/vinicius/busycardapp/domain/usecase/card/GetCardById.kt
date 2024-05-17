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
        var card: Card
        var state: CardState = CardState.NOT_SHARED
        getUserUseCase()
            .collect { user ->
                val myCards = user.myCards
                val sharedCards = user.sharedCards
                val draftCards = user.draftCards

                val isShared = sharedCards.any { it == param }
                val isMine = myCards.any { it == param } or draftCards.any { it == param }

                card = Card(
                    id = param,
                    isDraft = draftCards.any { it == param },
                )

                state = when {
                    isMine -> CardState.MINE
                    isShared -> CardState.SHARED
                    else -> CardState.NOT_SHARED
                }

                repository.get(card)
                    .collect { card ->
                        card.cardState = state
                        emit(card)
                    }
            }
    }
}