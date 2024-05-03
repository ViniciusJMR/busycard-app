package dev.vinicius.busycardapp.domain.usecase.card

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
    override suspend fun execute(param: Card) = flow {
        // Validation

        // Change owner to logged user
        param.owner = auth.getCurrentUserId()

        // Save
        val key = cardRepository.save(param)
        userRepository.saveMyCardId(auth.getCurrentUserId(), key)
        emit(Unit)
    }
}