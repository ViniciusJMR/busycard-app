package dev.vinicius.busycardapp.presentation.card_info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.model.usecase.card.GetCardById
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardInfoViewModel @Inject constructor(
    private val getCardById: GetCardById
): ViewModel() {
    private val _state = MutableStateFlow(CardInfoState())
    val state = _state.asStateFlow()

    companion object {
        val TAG = "CardInfoViewModel"
    }

    init {
        viewModelScope.launch {
            getCardById(1)
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