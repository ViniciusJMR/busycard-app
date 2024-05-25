package dev.vinicius.busycardapp.presentation.my_cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.usecase.card.read.GetDraftCards
import dev.vinicius.busycardapp.domain.usecase.card.read.GetMyCards
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCardsViewModel @Inject constructor(
    getMyCards: GetMyCards,
    getDraftCards: GetDraftCards
): ViewModel() {
    private val _state = MutableStateFlow(MyCardsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getMyCards()
                .onStart {
                    _state.update {
                        it.copy(
                            isMyCardsLoading = true
                        )
                    }
                }
                .collect{ cards ->
                    _state.update {
                        it.copy(
                            myCards = cards,
                            isMyCardsLoading = false
                        )
                    }
                }

            getDraftCards()
                .onStart {
                    _state.update {
                        it.copy(
                            isDraftCardsLoading = true
                        )
                    }
                }
                .collect{ cards ->
                    _state.update {
                        it.copy(
                            draftCards = cards,
                            isDraftCardsLoading = false
                        )
                    }
                }
        }
    }
}