package dev.vinicius.busycardapp.presentation.auth.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.repository.Auth
import dev.vinicius.busycardapp.domain.usecase.auth.GetUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val auth: Auth
): ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getUserUseCase()
                .collect { user ->
                    _state.update {
                        it.copy(
                            name = user.name,
                            surname = user.surname,
                            username = user.username,
                            email = user.email
                        )
                    }
                }
        }
    }

    fun logout() {
        auth.logOut()
    }
}