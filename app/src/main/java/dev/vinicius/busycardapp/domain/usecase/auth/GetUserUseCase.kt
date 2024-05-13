package dev.vinicius.busycardapp.domain.usecase.auth

import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.domain.model.user.User
import dev.vinicius.busycardapp.domain.repository.Auth
import dev.vinicius.busycardapp.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val auth: Auth,
    private val repository: IUserRepository<String, User>
): UseCase.NoParam<User>() {
    override suspend fun execute(): Flow<User> = flow {
        val userId = auth.getCurrentUserId()
        val user = repository.getUser(userId)
        emit(user)
    }
}