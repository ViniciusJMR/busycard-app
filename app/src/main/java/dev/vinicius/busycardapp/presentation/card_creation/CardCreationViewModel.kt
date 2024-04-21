package dev.vinicius.busycardapp.presentation.card_creation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.FieldType
import dev.vinicius.busycardapp.domain.model.card.TextType
import dev.vinicius.busycardapp.domain.usecase.card.SaveCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardCreationViewModel @Inject constructor(
    private val saveCard: SaveCard,
): ViewModel(){
    private val _state = MutableStateFlow(CardCreationState())
    val state = _state.asStateFlow()

    private val _effect: MutableStateFlow<CardCreationEffect?> = MutableStateFlow(null)
    val effect = _effect.asStateFlow()

    fun resetEffect() {
        _effect.update { null }
    }

    companion object {
        val TAG = "CardCreationViewModel"
    }

    fun onEvent(event: CardCreationEvent) {
        when (event) {
            is CardCreationEvent.CardEvent -> handleCardEvent(event)
            is CardCreationEvent.FieldEvent -> handleOnChangeFieldValue(event)
            is CardCreationEvent.ModalEvent -> handleOnModalEvent(event)
            is CardCreationEvent.DialogEvent -> handleOnDialogEvent(event)
        }
    }

    private fun handleCardEvent(event: CardCreationEvent.CardEvent) {
        when (event) {
            is CardCreationEvent.CardEvent.OnAddField -> {
                val field: Field = when(event.fieldType) {
                    FieldType.TEXT -> {
                        Field.TextField(
                            name = "text field 1",
                            value = "abcd",
                            offsetX = 200f,
                            offsetY = 200f,
                            textType = TextType.TEXT
                        )
                    }
                    FieldType.PHONE -> {
                        Field.TextField(
                            name = "Phone field 1",
                            value = "(99) 99999-9999",
                            offsetX = 200f,
                            offsetY = 200f,
                            textType = TextType.PHONE
                        )
                    }
                    FieldType.EMAIL -> {
                        Field.TextField(
                            name = "email field 1",
                            value = "place@holder.com",
                            offsetX = 200f,
                            offsetY = 200f,
                            textType = TextType.EMAIL
                        )
                    }
                    FieldType.ADDRESS -> TODO()
                    FieldType.IMAGE -> {
                        Field.ImageField(
                            name = "image field 1",
                            offsetX = 200f,
                            offsetY = 200f,
                        )
                    }
                }
                // Check if this doesn't make the entire CardSurface component to recompose
                val list = _state.value.cardFields.toMutableList()
                list.add(field)
                _state.update {
                    it.copy(
                        cardFields = list,
                        currentlySelectedField = field
                    )
                }
                Log.d(TAG, "handleCardEvent: available fields ${_state.value.cardFields}")
            }
            is CardCreationEvent.CardEvent.OnDeleteField -> {}
            is CardCreationEvent.CardEvent.OnSelectField -> {
                _state.update {
                    it.copy(
                        currentlySelectedField = event.field
                    )
                }
            }
            CardCreationEvent.CardEvent.OnSaveCard -> {
                val card = Card(
                    name = _state.value.cardName,
                    fields = _state.value.cardFields,
                    mainContact = _state.value.mainContactField?.value ?: "",
                ).apply {
                    image.uri = _state.value.cardImageUri
                }
                viewModelScope.launch {
                    saveCard(card)
                        .onStart {
                            _state.update {
                                it.copy(
                                    isLoading = true
                                )
                            }
                        }
                        .catch {
                            Log.d(TAG, "handleCardEvent: error: $it")
                            it.printStackTrace()
                        }
                        .collect{
                            Log.d(TAG, "handleCardEvent: Salvo com sucesso")
                            _effect.update { CardCreationEffect.ClosePage }
                        }
                }

            }

            is CardCreationEvent.CardEvent.OnChangeCard -> handleOnChangeCardValue(event)
        }
    }
    
    private fun handleOnChangeCardValue(event: CardCreationEvent.CardEvent.OnChangeCard) {
        when (event) {
            is CardCreationEvent.CardEvent.OnChangeCard.MainContact -> {
                _state.update { cardState ->
                    Log.d(TAG, "mainContactField: ${cardState.mainContactField} ")
                    Log.d(TAG, "event field: ${event.field}")
                    Log.d(TAG, "equals: ${cardState.mainContactField === event.field} ")
                    cardState.copy(
                        mainContactField = event.field.takeIf {
                            cardState.mainContactField !== event.field
                        },
                    )
                }
            }
            is CardCreationEvent.CardEvent.OnChangeCard.Info -> {
                _state.update { cardState ->
                    Log.d(TAG, "handleOnChangeCardValue: event: $event")
                    cardState.copy(
                        cardName = event.name,
                        cardImageUri = event.imagePath,
                        showCardInfoDialog = false,
                    )
                }
            }
        }
    }

    private fun handleOnChangeFieldValue(event: CardCreationEvent.FieldEvent) {
        when(event) {
            is CardCreationEvent.FieldEvent.OnTextFieldChange -> {
                _state.update {
                    it.copy(
                        currentlySelectedField =
                        ( it.currentlySelectedField as Field.TextField )
                            .apply {
                                event.name?.let {
                                    name = it
                                }
                                event.offsetX?.let {
                                    offsetX = it
                                }
                                event.offsetY?.let {
                                    offsetY = it
                                }
                                event.size?.let {
                                    size = it
                                }
                                event.textType?.let {
                                    textType = it
                                }
                                event.value?.let {
                                    value = it
                                }
                            },
                        showBottomSheet = false
                    )
                }
            }

            is CardCreationEvent.FieldEvent.OnTextFieldTypeChange -> {
                _state.update {
                    it.copy(
                        currentlySelectedField =
                        ( it.currentlySelectedField as Field.TextField )
                            .apply {
                                   textType = event.textType
                            },
                        showTextTypeDialog = false
                    )
                }
            }

            is CardCreationEvent.FieldEvent.OnImageFieldChange -> {
                _state.update {
                    it.copy(
                        currentlySelectedField =
                        ( it.currentlySelectedField as Field.ImageField)
                            .apply {
                                event.name?.let {
                                    name = it
                                }
                                event.offsetX?.let {
                                    offsetX = it
                                }
                                event.offsetY?.let {
                                    offsetY = it
                                }
                                event.size?.let {
                                    size = it
                                }
                                image.uri = event.uri
                            },
                        showBottomSheet = false
                    )
                }
            }
        }
    }

    // Analisar eficacia de utilizar classe Effect para assumir eventos na tela
    private fun handleOnModalEvent(event: CardCreationEvent.ModalEvent) {
        when(event) {
            CardCreationEvent.ModalEvent.OnDismissModalSheet -> {
                _state.update {
                    it.copy(
                        currentlySelectedField = null,
                        showBottomSheet = false,
                    )
                }
            }
            is CardCreationEvent.ModalEvent.OnShowBottomSheet -> {
                _state.update {
                    it.copy(
                        currentlySelectedField = event.field,
                        showBottomSheet = true,
                    )
                }
            }
        }
    }

    private fun handleOnDialogEvent(event: CardCreationEvent.DialogEvent) {
        when (event) {
            CardCreationEvent.DialogEvent.OnShowTextTypeDialog -> {
                _state.update {
                    it.copy(
                        showTextTypeDialog = true
                    )
                }
            }
            CardCreationEvent.DialogEvent.OnDismissTextTypeDialog -> {
                _state.update {
                    it.copy(
                        showTextTypeDialog = false
                    )
                }
            }

            CardCreationEvent.DialogEvent.OnDismissCardInfoDialog -> {
                _state.update {
                    it.copy(
                        showCardInfoDialog = false
                    )
                }
            }

            CardCreationEvent.DialogEvent.OnShowCardInfoDialog -> {
                _state.update {
                    it.copy(
                        showCardInfoDialog = true
                    )
                }
            }
        }
    }

}