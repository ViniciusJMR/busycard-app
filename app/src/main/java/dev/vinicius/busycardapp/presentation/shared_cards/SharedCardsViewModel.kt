package dev.vinicius.busycardapp.presentation.shared_cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.usecase.card.GetAll
import dev.vinicius.busycardapp.presentation.my_cards.MyCardsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedCardsViewModel @Inject constructor(
    getAll: GetAll
): ViewModel() {
    private val _state = MutableStateFlow(MyCardsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getAll()
                .onStart {
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
                .collect{ cards ->
                    _state.update {
                        it.copy(
                            cards = cards,
                            isLoading = false
                        )
                    }
                }
        }
    }
}