package dev.vinicius.busycardapp.domain.usecase.card.write

import android.util.Log
import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.model.card.CardImage
import dev.vinicius.busycardapp.domain.model.user.User
import dev.vinicius.busycardapp.domain.repository.Auth
import dev.vinicius.busycardapp.domain.repository.IRepository
import dev.vinicius.busycardapp.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveCardFromDraft @Inject constructor(
    private val auth: Auth,
    private val cardRepository: IRepository<String, Card>,
    private val userRepository: IUserRepository<String, User>,
): UseCase.NoSource<Card>() {

    companion object {
        private const val TAG = "SaveCardFromDraft"
    }

    override suspend fun execute(param: Card): Flow<Unit> = flow {
        val userId = auth.getCurrentUserId()
        Log.d(TAG, "Saving card from draft")
        val card = Card(
            id = null,
            name = param.name,
            owner = userId,
            mainContact = param.mainContact,
            image = CardImage(uri = param.image.uri),
            fields = param.fields,
            isDraft = false,
        )
        val cardId = cardRepository.save(card)

        userRepository.removeDraftCardId(userId, param.id!!)
        cardRepository.delete(param)
        userRepository.saveMyCardId(userId, cardId)
        emit(Unit)
    }

}