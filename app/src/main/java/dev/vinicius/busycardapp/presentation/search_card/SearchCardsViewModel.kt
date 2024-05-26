package dev.vinicius.busycardapp.presentation.search_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.usecase.card.read.GetAll
import dev.vinicius.busycardapp.domain.usecase.card.read.GetSharedCards
import dev.vinicius.busycardapp.presentation.shared_cards.SharedCardsEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchCardsViewModel @Inject constructor(
    private val getAll: GetAll
): ViewModel() {
    private val _state = MutableStateFlow(SearchCardsState())
    val state = _state.asStateFlow()

    private lateinit var cardsList: List<Card>

    private var searchJob: Job? = null

    init {
        gAllCards()
    }

    fun onEvent(event: SearchCardsEvent) {
        when (event) {
            is SearchCardsEvent.OnSearchQueryChange -> {
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

            SearchCardsEvent.Refresh -> gAllCards()
        }

    }

    private fun gAllCards() {
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
                    cardsList = cards
                }
        }
    }
}