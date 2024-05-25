package dev.vinicius.busycardapp.presentation.card_editing

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.model.card.CardImage
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.enums.FieldType
import dev.vinicius.busycardapp.domain.model.card.enums.LocationIconPosition
import dev.vinicius.busycardapp.domain.model.card.enums.TextType
import dev.vinicius.busycardapp.domain.usecase.card.read.GetCardById
import dev.vinicius.busycardapp.domain.usecase.card.write.SaveCard
import dev.vinicius.busycardapp.domain.usecase.card.write.SaveCardFromDraft
import dev.vinicius.busycardapp.presentation.card_detail.CardDetailViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CardEditingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getCardById: GetCardById,
    private val saveCard: SaveCard,
    private val saveCardFromDraft: SaveCardFromDraft
): ViewModel(){
    private val _state = MutableStateFlow(CardEditingState())
    val state = _state.asStateFlow()

    private val _fields = listOf<Field>().toMutableStateList()
    val fields: List<Field>
        get() = _fields

    /* Gambiarra para atualizar o UI. Quando um campo era deletado o valor
     * do campo excluído era passado para o próximo gerando um erro de sincronização
     * dos fields no ShowCardSection e os verdadeiros fields
     */
    private val _updateUi = MutableStateFlow(0)
    val updateUiState = _updateUi.asStateFlow()

    private val _effect: MutableStateFlow<CardEditingEffect?> = MutableStateFlow(null)
    val effect = _effect.asStateFlow()

    fun resetEffect() {
        _effect.update { null }
    }

    companion object {
        val TAG = "CardCreationViewModel"
    }

    init {
        viewModelScope.launch {
            val id = savedStateHandle.get<String>("id") ?: return@launch

            getCardById(id)
                .onStart {
                    Log.d(CardDetailViewModel.TAG, "Started")
                    _state.update {
                        it.copy(
                            isScreenLoading = true,
                        )
                    }
                }
                .catch {
                    Log.e(CardDetailViewModel.TAG, "Error: ${it.message}")
                    _state.update {
                        it.copy(
                            isScreenLoading = false,
                        )
                    }
                }
                .collect{ card ->
                    Log.d(CardDetailViewModel.TAG, "Collected:  $card")
                    _state.update {
                        it.copy(
                            isScreenLoading = false,
                            cardId = card.id,
                            cardImageUri = card.image.uri,
                            cardName = card.name,
                            isDraft = card.isDraft,
                            cardSize = card.cardSize,
                            cardColor = card.cardColor,
                            cardFields = card.fields.toMutableList(),
                            mainContactField = card.fields
                                .filterIsInstance<Field.TextField>()
                                .find { it.value == card.mainContact },
                        )
                    }
                    _fields.addAll(card.fields)
                }

        }
    }

    fun onEvent(event: CardEditingEvent) {
        when (event) {
            is CardEditingEvent.CardEvent -> handleCardEvent(event)
            is CardEditingEvent.FieldEvent -> handleOnChangeFieldValue(event)
            is CardEditingEvent.ModalEvent -> handleOnModalEvent(event)
            is CardEditingEvent.DialogEvent -> handleOnDialogEvent(event)
        }
    }

    private fun handleCardEvent(event: CardEditingEvent.CardEvent) {
        when (event) {
            is CardEditingEvent.CardEvent.OnAddField -> {
                val field: Field = when(event.fieldType) {
                    FieldType.TEXT -> {
                        Field.TextField(
                            name = "text field 1",
                            value = "abcd",
                            offsetX = 200,
                            offsetY = 200,
                            size = 20,
                            textType = TextType.TEXT
                        )
                    }
                    FieldType.PHONE -> {
                        Field.TextField(
                            name = "Phone field 1",
                            value = "(99) 99999-9999",
                            offsetX = 200,
                            offsetY = 200,
                            size = 20,
                            textType = TextType.PHONE
                        )
                    }
                    FieldType.EMAIL -> {
                        Field.TextField(
                            name = "email field 1",
                            value = "place@holder.com",
                            offsetX = 200,
                            offsetY = 200,
                            size = 20,
                            textType = TextType.EMAIL
                        )
                    }
                    FieldType.ADDRESS -> {
                        Field.AddressField(
                            name = "address field 1",
                            offsetX = 200,
                            offsetY = 200,
                            size = 20,
                            iconPosition = LocationIconPosition.LEFT,
                            iconSize = 40,
                        )
                    }
                    FieldType.IMAGE -> {
                        Field.ImageField(
                            name = "image field 1",
                            offsetX = 200,
                            offsetY = 200,
                            size = 50,
                        )
                    }
                }
                // Check if this doesn't make the entire CardSurface component to recompose
                val list = _state.value.cardFields.toMutableList()
                list.add(field)
                _fields.add(field)
                _state.update {
                    it.copy(
                        cardFields = list,
                        currentlySelectedField = field
                    )
                }
                Log.d(TAG, "handleCardEvent: available fields ${_fields}")
            }
            is CardEditingEvent.CardEvent.OnDeleteField -> {
                val list = _state.value.cardFields.toMutableList()
                Log.d(TAG, "handleCardEvent: removed ${list.remove(event.field)}")
                Log.d(TAG, "handleCardEvent: item ${event.field}")
                _state.update {
                    it.copy(
                        cardFields = list,
                        currentlySelectedField = null,
                    )
                }
                val a = _fields.remove(event.field)
                Log.d(TAG, "handleCardEvent: removed ${a}")
                Log.d(TAG, "handleCardEvent: available fields ${_fields}")
            }
            is CardEditingEvent.CardEvent.OnSelectField -> {
                Log.d(TAG, "handleCardEvent: selected field: ${event.field}")
                _state.update {
                    it.copy(
                        currentlySelectedField = event.field
                    )
                }
            }
            is CardEditingEvent.CardEvent.OnSaveEvent -> {
                val card = Card(
                    id = _state.value.cardId,
                    name = _state.value.cardName,
                    fields = _fields,
                    cardSize = _state.value.cardSize,
                    cardColor = _state.value.cardColor,
                    mainContact = _state.value.mainContactField?.value ?: "",
                ).apply {
                    _state.value.cardImageUri?.let {
                        image = CardImage(uri = it)
                    }

                    if (event is CardEditingEvent.CardEvent.OnSaveEvent.OnSaveCardAsDraft) {
                        isDraft = true
                    }

                }
                viewModelScope.launch {
                    val useCase = if (_state.value.isDraft)
                        saveCardFromDraft
                    else
                        saveCard

                    useCase(card)
                        .onStart {
                            _state.update {
                                it.copy(
                                    isScreenLoading = true,
                                    showSaveDialog = false,
                                )
                            }
                        }
                        .catch {
                            Log.d(TAG, "handleCardEvent: error: $it")
                            it.printStackTrace()
                            _state.update {
                                it.copy(
                                    isScreenLoading = false,
                                )
                            }
                        }
                        .collect{
                            Log.d(TAG, "handleCardEvent: Salvo com sucesso")
                            _effect.update { CardEditingEffect.ClosePage }
                            _state.update {
                                it.copy(
                                    isScreenLoading = false,
                                )
                            }
                        }
                }
            }
            is CardEditingEvent.CardEvent.OnDragField -> {
                _state.update {
                    it.copy(
                        currentlySelectedField = event.field.apply { offsetX = event.offsetX; offsetY = event.offsetY },
                        cardFields = it.cardFields
                    )
                }
                _updateUi.update { random() }
            }
            is CardEditingEvent.CardEvent.OnChangeCard -> handleOnChangeCardValue(event)
        }
    }

    private fun handleOnChangeCardValue(event: CardEditingEvent.CardEvent.OnChangeCard) {
        when (event) {
            is CardEditingEvent.CardEvent.OnChangeCard.MainContact -> {
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
            is CardEditingEvent.CardEvent.OnChangeCard.Info -> {
                _state.update { cardState ->
                    Log.d(TAG, "handleOnChangeCardValue: event: $event")
                    cardState.copy(
                        cardName = event.name,
                        cardImageUri = event.imagePath,
                        showCardInfoDialog = false,
                        cardSize = event.size,
                        cardColor = event.color,
                    )
                }
            }
        }
    }

    private fun handleOnChangeFieldValue(event: CardEditingEvent.FieldEvent) {
        when(event) {
            is CardEditingEvent.FieldEvent.OnTextFieldChange -> {
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
                                event.font?.let {
                                    font = it
                                }
                            },
                        showBottomSheet = false
                    )
                }
            }

            is CardEditingEvent.FieldEvent.OnTextFieldTypeChange -> {
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

            is CardEditingEvent.FieldEvent.OnImageFieldChange -> {
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
                                image = CardImage(uri = event.uri)
                            },
                        showBottomSheet = false
                    )
                }
            }

            is CardEditingEvent.FieldEvent.OnAddressFieldChange -> {
                _state.update {
                    it.copy(
                        currentlySelectedField =
                        ( it.currentlySelectedField as Field.AddressField)
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
                                event.textLocalization?.let {
                                    textLocalization = it
                                }
                                event.localization?.let {
                                    localization = it
                                }
                                event.font?.let {
                                    font = it
                                }
                                event.iconSize?.let {
                                    iconSize = it
                                }
                                event.iconPosition?.let {
                                    iconPosition = it
                                }
                            },
                        showBottomSheet = false
                    )
                }
                Log.d(TAG, "handleOnChangeFieldValue: currently: ${_state.value.currentlySelectedField}")
                Log.d(TAG, "handleOnChangeFieldValue: it: ${event.localization}")
            }
        }
    }

    // Analisar eficacia de utilizar classe Effect para assumir eventos na tela
    private fun handleOnModalEvent(event: CardEditingEvent.ModalEvent) {
        when(event) {
            CardEditingEvent.ModalEvent.OnDismissModalSheet -> {
                _state.update {
                    it.copy(
                        currentlySelectedField = null,
                        showBottomSheet = false,
                    )
                }
            }
            is CardEditingEvent.ModalEvent.OnShowBottomSheet -> {
                _state.update {
                    it.copy(
                        currentlySelectedField = event.field,
                        showBottomSheet = true,
                    )
                }
            }
        }
    }

    private fun handleOnDialogEvent(event: CardEditingEvent.DialogEvent) {
        when (event) {
            CardEditingEvent.DialogEvent.OnShowTextTypeDialog -> {
                _state.update {
                    it.copy(
                        showTextTypeDialog = true
                    )
                }
            }
            CardEditingEvent.DialogEvent.OnDismissTextTypeDialog -> {
                _state.update {
                    it.copy(
                        showTextTypeDialog = false
                    )
                }
            }

            CardEditingEvent.DialogEvent.OnDismissCardInfoDialog -> {
                _state.update {
                    it.copy(
                        showCardInfoDialog = false
                    )
                }
            }

            CardEditingEvent.DialogEvent.OnShowCardInfoDialog -> {
                _state.update {
                    it.copy(
                        showCardInfoDialog = true
                    )
                }
            }
            CardEditingEvent.DialogEvent.OnDismissSaveDialog ->
                _state.update {
                    it.copy(
                        showSaveDialog = false
                    )
                }
            CardEditingEvent.DialogEvent.OnShowSaveDialog ->
                _state.update {
                    it.copy(
                        showSaveDialog = true
                    )
                }
        }
    }

    private fun random() = Random.nextInt(0, 10000)
}