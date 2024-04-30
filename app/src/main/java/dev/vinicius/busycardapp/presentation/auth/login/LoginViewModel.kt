package dev.vinicius.busycardapp.presentation.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.model.auth.LoginAccount
import dev.vinicius.busycardapp.domain.usecase.auth.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
): ViewModel() {
    companion object {
        val TAG = "LoginViewModel"
    }

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effect: MutableStateFlow<LoginEffect?> = MutableStateFlow(null)
    val effect = _effect.asStateFlow()

    fun resetEffect() {
        _effect.update { null }
    }

    fun onEvent(event: LoginEvent) {
        when(event){
            is LoginEvent.OnEmailChange -> {
                _state.update {
                    it.copy(
                        email = event.email
                    )
                }
            }
            is LoginEvent.OnPasswordChange -> {
                _state.update {
                    it.copy(
                        password = event.password
                    )
                }
            }
            LoginEvent.OnLogin -> {
                viewModelScope.launch {
                    val account = LoginAccount(_state.value.email, _state.value.password)
                    loginUseCase(account)
                        .catch {
                            Log.e(TAG, "onEvent: Error when logging in: ${it.message}", )
                        }
                        .collect {
                            _effect.update {
                                LoginEffect.NavigateToHome
                            }
                        }
                }
            }

            LoginEvent.OnSignInClicked -> {
                _effect.update {
                    LoginEffect.NavigateToCreateAccount
                }
            }
        }
    }

}