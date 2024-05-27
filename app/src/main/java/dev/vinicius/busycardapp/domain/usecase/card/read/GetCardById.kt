package dev.vinicius.busycardapp.domain.usecase.card.read

import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.domain.repository.IRepository
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.model.card.enums.CardState
import dev.vinicius.busycardapp.domain.usecase.auth.GetUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCardById @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val repository: IRepository<String, Card>
): UseCase< Pair<String, Boolean>, Card>() {
    override suspend fun execute(param:Pair<String, Boolean> ): Flow<Card> = flow {
        var card: Card
        var state: CardState = CardState.NOT_SHARED
        val id = param.first
        val searchInDraft = param.second
        getUserUseCase()
            .collect { user ->
                val myCards = user.myCards
                val sharedCards = user.sharedCards
                val draftCards = user.draftCards

                val isShared = sharedCards.any { it == id}
                val isMine = myCards.any { it == id } or draftCards.any { it == id }

                card = Card(
                    id = id,
                    isDraft = searchInDraft,
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