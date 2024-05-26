package dev.vinicius.busycardapp.presentation.my_cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.usecase.card.read.GetDraftCards
import dev.vinicius.busycardapp.domain.usecase.card.read.GetMyCards
import dev.vinicius.busycardapp.presentation.search_card.SearchCardsEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

    private lateinit var myCardsList: List<Card>
    private lateinit var draftCardsList: List<Card>

    private var searchJob: Job? = null


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
                    myCardsList = cards
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
                    draftCardsList = cards
                }
        }
    }


    fun onEvent(event: MyCardsEvent) {
        when (event) {
            is MyCardsEvent.OnSearchQueryChange -> {
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
                            isMyCardsLoading = true,
                            isDraftCardsLoading = true
                        )
                    }
                    _state.update {
                        it.copy(
                            myCards = myCardsList.filter { it.name.contains(event.query, true) },
                            draftCards = draftCardsList.filter { it.name.contains(event.query, true) },
                        )
                    }
                    _state.update {
                        it.copy(
                            isMyCardsLoading = false,
                            isDraftCardsLoading = false
                        )
                    }
                }
            }
        }

    }
}