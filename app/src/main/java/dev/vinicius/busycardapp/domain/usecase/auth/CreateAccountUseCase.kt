package dev.vinicius.busycardapp.domain.usecase.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.domain.model.auth.CreateAccount
import dev.vinicius.busycardapp.domain.model.user.User
import dev.vinicius.busycardapp.domain.repository.Auth
import dev.vinicius.busycardapp.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val auth: Auth,
    private val repository: IUserRepository<String, User>
): UseCase.NoSource<CreateAccount>() {
    override suspend fun execute(param: CreateAccount): Flow<Unit> = flow {
        auth.signIn(param.email, param.password)
        // TODO: Remove this workaround
        val uid = Firebase.auth.currentUser!!.uid
        val user = User(
            uid,
            param.username,
            param.name,
            param.surname,
            param.email
        )
        repository.save(user)
        emit(Unit)
    }
}