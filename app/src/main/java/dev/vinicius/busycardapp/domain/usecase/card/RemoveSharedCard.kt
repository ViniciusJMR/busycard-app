package dev.vinicius.busycardapp.domain.usecase.card

import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.domain.model.user.User
import dev.vinicius.busycardapp.domain.repository.Auth
import dev.vinicius.busycardapp.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoveSharedCard @Inject constructor(
    private val auth: Auth,
    private val repository: IUserRepository<String, User>,
): UseCase.NoSource<String>() {
    override suspend fun execute(param: String): Flow<Unit> = flow {
        val userId = auth.getCurrentUserId()
        repository.removeSharedCardId(userId, param)
        emit(Unit)
    }

}