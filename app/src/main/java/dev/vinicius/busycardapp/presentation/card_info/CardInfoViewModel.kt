package dev.vinicius.busycardapp.presentation.card_info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.model.usecase.card.GetCardById
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
                .collect{
                    Log.d(TAG, "Collected")
                }
        }
    }

    fun onEvent(event: CardInfoEvent){
        when(event){
            else -> {}
        }
    }
}