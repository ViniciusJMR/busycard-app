package dev.vinicius.busycardapp.presentation.shared_cards

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.usecase.card.read.GetSharedCards
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedCardsViewModel @Inject constructor(
    private val getSharedCards: GetSharedCards
): ViewModel() {
    private val _state = MutableStateFlow(SharedCardsState())
    val state = _state.asStateFlow()

    private lateinit var cardsList: List<Card>

    private var searchJob: Job? = null

    companion object {
        private const val TAG = "SharedCardsViewModel"
    }

    init {
        gSharedCards()
    }

    fun onEvent(event: SharedCardsEvent) {
        when (event) {
            is SharedCardsEvent.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        searchQuery = event.query
                    )
                }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    _state.update {
                        it.copy(
                            cards = cardsList.filter { it.name.contains(event.query, true) },
                        )
                    }
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }

            SharedCardsEvent.Refresh -> gSharedCards()
        }
    }

    private fun gSharedCards() {
        viewModelScope.launch {
            getSharedCards()
                .onStart {
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
                .catch {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    Log.e(TAG, "gSharedCards: Error: ${it.message}")
                }
                .collect{ cards ->
                    _state.update {
                        it.copy(
                            cards = cards,
                            isLoading = false
                        )
                    }
                    cardsList = cards
                }
        }
    }


}