package dev.vinicius.busycardapp.domain.usecase.auth
import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.data.repository.impl.AuthRepository
import dev.vinicius.busycardapp.domain.model.auth.CreateAccount
import dev.vinicius.busycardapp.domain.model.auth.LoginAccount
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val auth: AuthRepository
): UseCase.NoSource<LoginAccount>() {
    override suspend fun execute(param: LoginAccount): Flow<Unit> = flow {
        emit(auth.logIn(param.email, param.password))
    }
}
