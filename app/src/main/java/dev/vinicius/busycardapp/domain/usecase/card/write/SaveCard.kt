package dev.vinicius.busycardapp.domain.usecase.card.write

import android.util.Log
import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.model.user.User
import dev.vinicius.busycardapp.domain.repository.Auth
import dev.vinicius.busycardapp.domain.repository.IRepository
import dev.vinicius.busycardapp.domain.repository.IUserRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveCard @Inject constructor(
    private val auth: Auth,
    private val cardRepository: IRepository<String, Card>,
    private val userRepository: IUserRepository<String, User>,
): UseCase.NoSource<Card>() {

    companion object {
        private const val TAG = "SaveCard"
    }

    override suspend fun execute(param: Card) = flow {
        // Validation

        // Change owner to logged user
        param.owner = auth.getCurrentUserId()

        Log.d(TAG, "Card: $param")
        // Save
        val key = cardRepository.save(param)
        if (param.isDraft) {
            userRepository.saveDraftCardId(auth.getCurrentUserId(), key)
        } else {
            userRepository.saveMyCardId(auth.getCurrentUserId(), key)
        }
        emit(Unit)
    }
}