package dev.vinicius.busycardapp.domain.usecase.card.read

import android.util.Log
import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.model.user.User
import dev.vinicius.busycardapp.domain.repository.Auth
import dev.vinicius.busycardapp.domain.repository.IRepository
import dev.vinicius.busycardapp.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDraftCards @Inject constructor(
    private val auth: Auth,
    private val userRepository: IUserRepository<String, User>,
    private val cardRepository: IRepository<String, Card>
): UseCase.NoParam<List<Card>>() {

    companion object {
        val TAG = "GetDraftCards"
    }

    override suspend fun execute(): Flow<List<Card>> = flow {
        val userId = auth.getCurrentUserId()
        val cardsId = userRepository.getDraftCardsId(userId)
        Log.d(TAG, "execute: cardsId = $cardsId")
        if (cardsId.isEmpty()){
            emit(emptyList())
            return@flow
        }
        cardRepository.getByIds(cardsId)
            .collect{
                Log.d(TAG, "execute: cards = $it")
                emit(it.filter { it.isDraft })
            }
    }
}