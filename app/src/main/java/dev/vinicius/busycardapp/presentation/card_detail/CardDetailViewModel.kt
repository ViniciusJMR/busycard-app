package dev.vinicius.busycardapp.presentation.card_detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.usecase.card.GetCardById
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCardById: GetCardById
): ViewModel() {
    private val _state = MutableStateFlow(CardDetailState())
    val state = _state.asStateFlow()

    companion object {
        val TAG = "CardInfoViewModel"
    }

    init {
        viewModelScope.launch {
            val id = savedStateHandle.get<String>("id") ?: return@launch
            Log.d(TAG, "id: $id")
            getCardById(id)
                .onStart {
                    Log.d(TAG, "Started")
                    _state.update {
                        it.copy(
                            isLoading = true,
                        )
                    }
                }
                .catch {
                    Log.e(TAG, "Error: ${it.message}")
                }
                .collect{ card ->
                    Log.d(TAG, "Collected:  $card")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            name = card.name,
                            owner = card.owner,
                            fields = card.fields
                        )
                    }
                }
        }
    }

    fun onEvent(event: CardInfoEvent){
        when(event){
            else -> {}
        }
    }
}