package dev.vinicius.busycardapp.presentation.card_detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.model.card.enums.CardState
import dev.vinicius.busycardapp.domain.usecase.card.delete.DeleteCardById
import dev.vinicius.busycardapp.domain.usecase.card.read.GetCardById
import dev.vinicius.busycardapp.domain.usecase.card.delete.RemoveSharedCard
import dev.vinicius.busycardapp.domain.usecase.card.write.SaveSharedCard
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
    private val getCardById: GetCardById,
    private val saveSharedCard: SaveSharedCard,
    private val removeSharedCard: RemoveSharedCard,
    private val deleteCardById: DeleteCardById,
): ViewModel() {

    companion object {
        val TAG = "CardInfoViewModel"
    }

    private val _state = MutableStateFlow(CardDetailState())
    val state = _state.asStateFlow()

    private val _effect = MutableStateFlow<CardDetailEffect?>(null)
    val effect = _effect.asStateFlow()

    fun resetEffect() {
        _effect.update { null }
    }

    init {
        viewModelScope.launch {
            val id = savedStateHandle.get<String>("id") ?: return@launch
            val isFromDraft = savedStateHandle.get<Boolean>("isFromDraft") ?: false
            val saveAsSharedCard = savedStateHandle.get<Boolean>("saveAsSharedCard") ?: false
            Log.d(TAG, "id: $id")
            getCardById(Pair(id, isFromDraft))
                .onStart {
                    Log.d(TAG, "Started")
                    _state.update {
                        it.copy(
                            isScreenLoading = true,
                        )
                    }
                }
                .catch {
                    Log.e(TAG, "Error: ${it.message}")
                    it.printStackTrace()
                }
                .collect{ card ->
                    Log.d(TAG, "Collected:  $card")
                    _state.update {
                        it.copy(
                            isScreenLoading = false,
                            id = card.id ?: "",
                            cardState = card.cardState,
                            imagePath = card.image.uri?.toString() ?: "",
                            name = card.name,
                            owner = card.owner,
                            cardSize = card.cardSize,
                            cardColor = card.cardColor,
                            fields = card.fields
                        )
                    }
                }

            if (saveAsSharedCard) {
                onEvent(CardInfoEvent.CardEvent.OnSaveToSharedCard)
            }
        }
    }

    fun onEvent(event: CardInfoEvent){
        when(event){
            is CardInfoEvent.CardEvent -> handleCardEvent(event)
            is CardInfoEvent.DialogEvent -> handleDialogEvent(event)
            is CardInfoEvent.ModalEvent -> handleModalEvent(event)
        }
    }

    private fun handleCardEvent(event: CardInfoEvent.CardEvent) {
        when (event) {
            CardInfoEvent.CardEvent.OnSaveToSharedCard -> {
                viewModelScope.launch {
                    saveSharedCard(_state.value.id)
                        .onStart {
                            _state.update {
                                it.copy(
                                    isBottomSheetLoading = true
                                )
                            }
                        }
                        .catch {
                            _state.update {
                                it.copy(
                                    isBottomSheetLoading = false
                                )
                            }
                            _effect.update {
                                CardDetailEffect.ShowSnackbar("Erro ao adicionar cartão aos favoritos")
                            }
                        }
                        .collect {
                            _state.update {
                                it.copy(
                                    cardState = CardState.SHARED,
                                    isBottomSheetLoading = false
                                )
                            }
                            _effect.update {
                                CardDetailEffect.ShowSnackbar("Cartão adicionado aos favoritos")
                            }
                        }
                }
            }
            CardInfoEvent.CardEvent.OnDeleteFromSharedCard -> {
                viewModelScope.launch {
                    removeSharedCard(_state.value.id)
                        .onStart {
                            _state.update {
                                it.copy(
                                    isBottomSheetLoading = true
                                )
                            }
                        }
                        .catch {
                            _state.update {
                                it.copy(
                                    isBottomSheetLoading = false
                                )
                            }
                            _effect.update {
                                CardDetailEffect.ShowSnackbar("Erro ao remover cartão dos favoritos")
                            }
                        }
                        .collect {
                            _state.update {
                                it.copy(
                                    cardState = CardState.NOT_SHARED,
                                    isBottomSheetLoading = false
                                )
                            }
                            _effect.update {
                                CardDetailEffect.ShowSnackbar("Cartão removido dos favoritos")
                            }
                        }
                }
            }
            CardInfoEvent.CardEvent.OnDeleteCard -> {
                viewModelScope.launch {
                    val isFromDraft = savedStateHandle.get<Boolean>("isFromDraft") ?: false

                    deleteCardById(Pair(_state.value.id, isFromDraft))
                        .onStart {
                            _state.update {
                                it.copy(
                                    isScreenLoading = true
                                )
                            }
                        }
                        .catch {
                            _state.update {
                                it.copy(
                                    isScreenLoading = false
                                )
                            }
                        }
                        .collect {
                            _state.update {
                                it.copy(
                                    isScreenLoading = false
                                )
                            }

                            _effect.update { CardDetailEffect.ClosePage }
                        }
                    }

            }
        }
    }

    private fun handleDialogEvent(event: CardInfoEvent.DialogEvent) {
        when (event) {
            CardInfoEvent.DialogEvent.OnShowShareDialog -> {
                _state.update {
                    it.copy(
                        showShareDialog = true
                    )
                }
            }
            CardInfoEvent.DialogEvent.OnDismissShareDialog -> {
                _state.update {
                    it.copy(
                        showShareDialog = false
                    )
                }
            }

            CardInfoEvent.DialogEvent.OnDismissDeleteDialog ->
                _state.update {
                    it.copy(
                        showDeleteDialog = false
                    )
                }

            CardInfoEvent.DialogEvent.OnShowDeleteDialog ->
                _state.update {
                    it.copy(
                        showDeleteDialog = true
                    )
                }

        }
    }

    private fun handleModalEvent(event: CardInfoEvent.ModalEvent) {
        when (event) {
            CardInfoEvent.ModalEvent.OnShowBottomSheet -> {
                _state.update {
                    it.copy(
                        showBottomSheet = true
                    )
                }
            }
            CardInfoEvent.ModalEvent.OnDismissModalSheet -> {
                _state.update {
                    it.copy(
                        showBottomSheet = false
                    )
                }
            }
        }
    }

}