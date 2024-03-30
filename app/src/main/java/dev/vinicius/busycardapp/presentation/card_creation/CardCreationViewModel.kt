package dev.vinicius.busycardapp.presentation.card_creation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.FieldType
import dev.vinicius.busycardapp.domain.model.card.TextType
import dev.vinicius.busycardapp.domain.model.usecase.card.SaveCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CardCreationViewModel @Inject constructor(
    private val saveCard: SaveCard,
): ViewModel(){
    private val _state = MutableStateFlow(CardCreationState())
    val state = _state.asStateFlow()


    fun onEvent(event: CardCreationEvent) {
        when (event) {
            is CardCreationEvent.CardEvent -> handleCardEvent(event)
            is CardCreationEvent.FieldEvent -> handleOnChangeFieldValue(event)
            is CardCreationEvent.ModalEvent -> handleOnModalEvent(event)
        }
    }

    private fun handleCardEvent(event: CardCreationEvent.CardEvent) {
        when (event) {
            is CardCreationEvent.CardEvent.OnAddField -> {
                val field: Field = when(event.fieldType) {
                    FieldType.TEXT -> {
                        Field.TextField(
                            name = "text field 1",
                            value = "Pintao",
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
                            name = "Phone field 1",
                            value = "(99) 99999-9999",
                            offsetX = 200f,
                            offsetY = 200f,
                            textType = TextType.EMAIL
                        )
                    }
                    FieldType.ADDRESS -> TODO()
                    FieldType.IMAGE -> TODO()
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
            }
            is CardCreationEvent.CardEvent.OnDeleteField -> {}
            is CardCreationEvent.CardEvent.OnSelectField -> {
                _state.update {
                    it.copy(
                        currentlySelectedField = event.field
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
                                value = event.field.value
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
}