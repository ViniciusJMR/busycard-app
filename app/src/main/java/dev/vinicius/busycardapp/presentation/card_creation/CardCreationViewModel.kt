package dev.vinicius.busycardapp.presentation.card_creation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.FieldType
import dev.vinicius.busycardapp.domain.model.card.TextType
import dev.vinicius.busycardapp.domain.model.usecase.card.SaveCard
import dev.vinicius.busycardapp.presentation.card_creation.CardCreationEvent.OnAddField
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
            is OnAddField -> handleOnAddField(event.fieldType)
            is CardCreationEvent.OnDeleteField -> handleOnDelete(event.field)
            is CardCreationEvent.OnSelectField -> handleOnSelectField(event.field)
            CardCreationEvent.OnDismissModalSheet -> handleOnDismissModalSheet()
            is CardCreationEvent.OnShowBottomSheet -> handleOnShowBottomSheet(event.field)
        }
    }

    private fun handleOnShowBottomSheet(field: Field?) {
        _state.update {
            it.copy(
                currentlySelectedField = field,
                showBottomSheet = true,
            )
        }
    }

    private fun handleOnDismissModalSheet() {
        _state.update {
            it.copy(
                currentlySelectedField = null,
                showBottomSheet = false,
            )
        }

    }

    private fun handleOnAddField(fieldType: FieldType) {
        val field: Field = when(fieldType) {
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
        val list = _state.value.cardFields.toMutableList()
        list.add(field)
        _state.update {
            it.copy(
                cardFields = list
            )
        }
    }

    private fun handleOnSelectField(field: Field?) {
        _state.update {
            it.copy(
                currentlySelectedField = field
            )
        }
    }

    private fun handleOnDelete(field: Field) {

    }

}