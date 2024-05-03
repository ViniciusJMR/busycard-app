package dev.vinicius.busycardapp.domain.usecase.card

import android.util.Log
import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.model.user.User
import dev.vinicius.busycardapp.domain.repository.IRepository
import dev.vinicius.busycardapp.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMyCards @Inject constructor(
    private val userRepository: IUserRepository<String, User>,
    private val cardRepository: IRepository<String, Card>
): UseCase<String, List<Card>>() {
    companion object{
        val TAG = "GetMyCards"
    }

    override suspend fun execute(param: String): Flow<List<Card>> = flow {
        val cardsId = userRepository.getMyCardsId(param)
        Log.d(TAG, "execute: cardsId = $cardsId")
        cardRepository.getByIds(cardsId)
            .collect{
                Log.d(TAG, "execute: cards = $it")
                emit(it)
            }
    }
}