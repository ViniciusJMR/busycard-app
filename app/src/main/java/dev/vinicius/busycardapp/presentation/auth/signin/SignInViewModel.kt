package dev.vinicius.busycardapp.presentation.auth.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.model.auth.CreateAccount
import dev.vinicius.busycardapp.domain.model.auth.LoginAccount
import dev.vinicius.busycardapp.domain.usecase.auth.CreateAccountUseCase
import dev.vinicius.busycardapp.domain.usecase.auth.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val createAccount: CreateAccountUseCase
) : ViewModel() {
    companion object {
        val TAG = "LoginViewModel"
    }

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _effect: MutableStateFlow<SignInEffect?> = MutableStateFlow(null)
    val effect = _effect.asStateFlow()

    fun resetEffect() {
        _effect.update { null }
    }

    fun onEvent(event: SignInEvent) {
        when(event){
            is SignInEvent.OnEmailChange -> {
                _state.update {
                    it.copy(
                        email = event.email
                    )
                }
            }
            is SignInEvent.OnPasswordChange -> {
                _state.update {
                    it.copy(
                        password = event.password
                    )
                }
            }
            is SignInEvent.OnPassword2Change -> {
                _state.update {
                    it.copy(
                        password2 = event.password
                    )
                }
            }

            is SignInEvent.OnNameChange -> {
                _state.update {
                    it.copy(
                        name = event.name
                    )
                }
            }
            is SignInEvent.OnSurnameChange -> {
                _state.update {
                    it.copy(
                        surname = event.surname
                    )
                }
            }
            is SignInEvent.OnUsernameChange -> {
                _state.update {
                    it.copy(
                        username = event.username
                    )
                }
            }

            SignInEvent.OnSignIn-> {
                viewModelScope.launch {
                    val account = CreateAccount(
                        _state.value.username,
                        _state.value.name,
                        _state.value.surname,
                        _state.value.email,
                        _state.value.password,
                        _state.value.password2
                    )

                    createAccount(account)
                        .catch {
                            Log.e(TAG, "onEvent: Error when logging in: ${it.message}", )
                        }
                        .collect {
                            _effect.update {
                                SignInEffect.NavigateToHome
                            }
                        }
                }
            }
        }
    }

}