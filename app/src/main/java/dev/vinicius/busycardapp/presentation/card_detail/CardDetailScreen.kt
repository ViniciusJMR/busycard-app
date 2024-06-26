package dev.vinicius.busycardapp.presentation.card_detail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.CardMembership
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.domain.model.card.enums.CardState
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.enums.CardColor
import dev.vinicius.busycardapp.presentation.card_detail.component.CardInfoAddressField
import dev.vinicius.busycardapp.presentation.card_detail.component.CardInfoImageField
import dev.vinicius.busycardapp.presentation.card_detail.component.CardInfoTextField
import dev.vinicius.busycardapp.presentation.card_detail.component.CompactAddressFieldComponent
import dev.vinicius.busycardapp.presentation.card_detail.component.CompactTextFieldComponent
import dev.vinicius.busycardapp.presentation.card_detail.component.DialogComponent
import dev.vinicius.busycardapp.presentation.destinations.CardDetailScreenDestination
import dev.vinicius.busycardapp.presentation.destinations.CardEditingScreenDestination
import dev.vinicius.busycardapp.presentation.destinations.MyCardsScreenDestination
import dev.vinicius.busycardapp.ui.theme.BusyCardAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph
@Destination
@Composable
fun CardDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: CardDetailViewModel = hiltViewModel(),
    saveAsSharedCard: Boolean = false,
    isFromDraft: Boolean = false,
    navigator: DestinationsNavigator,
    id: String,
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState()
    val event = viewModel::onEvent
    val TAG = "CardInfoScreen"

    val sheetState = rememberModalBottomSheetState()

    val snackbarHostState = remember { SnackbarHostState() }


    if (state.showShareDialog) {
        CardDetailShareDialog(
            onConfirm = { event(CardInfoEvent.DialogEvent.OnDismissShareDialog) },
            onDismiss = { event(CardInfoEvent.DialogEvent.OnDismissShareDialog) },
            id = id,
        )
    }

    if (state.showDeleteDialog) {
        CardDetailDeleteDialog(
            onConfirm = { event(CardInfoEvent.CardEvent.OnDeleteCard) },
            onDismiss = { event(CardInfoEvent.DialogEvent.OnDismissDeleteDialog) },
        )
    }

    if (state.showBottomSheet) {
        CardInfoBottomSheet(
            cardState = state.cardState,
            isBottomSheetLoading = state.isBottomSheetLoading,
            onAddToSharedCards = { event(CardInfoEvent.CardEvent.OnSaveToSharedCard) },
            onDeleteFromSharedCards = { event(CardInfoEvent.CardEvent.OnDeleteFromSharedCard) },
            onDismissModalSheet = { event(CardInfoEvent.ModalEvent.OnDismissModalSheet) },
            imagePath = state.imagePath,
            fields = state.fields,
        )
    }


    Scaffold(
        Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = state.name) 
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigator.navigateUp()
                    }) {
                        Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = "")
                    }
                },
                actions = {
                    if (state.cardState == CardState.MINE) {
                        IconButton(
                            onClick = {
                                navigator.navigate(CardEditingScreenDestination(id = id, isFromDraft = isFromDraft)) {
                                    popUpTo(MyCardsScreenDestination)
                                }
                            }
                        ) {
                            Icon(Icons.Outlined.Edit, contentDescription = "")
                        }
                        IconButton(
                            onClick = { event(CardInfoEvent.DialogEvent.OnShowDeleteDialog) }
                        ) {
                            Icon(Icons.Outlined.Delete, contentDescription = "")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            Column (
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!isFromDraft) {
                    if (state.cardState != CardState.MINE) {
                        val onEvent: CardInfoEvent.CardEvent
                        val icon: ImageVector

                        if (state.cardState == CardState.SHARED) {
                            onEvent = CardInfoEvent.CardEvent.OnDeleteFromSharedCard
                            icon = Icons.Filled.Star
                        } else {
                            onEvent = CardInfoEvent.CardEvent.OnSaveToSharedCard
                            icon = Icons.Outlined.StarOutline
                        }

                        FloatingActionButton(
                            onClick = {
                                event(onEvent)
                            }
                        ) {
                            Icon(icon, contentDescription = null)
                        }
                    }
                    FloatingActionButton(
                        onClick = {
                            event( CardInfoEvent.DialogEvent.OnShowShareDialog )
                        }
                    ) {
                        Icon(Icons.Outlined.Share, contentDescription = "")
                    }
                }
                FloatingActionButton(
                    onClick = {
                        event( CardInfoEvent.ModalEvent.OnShowBottomSheet )
                    }
                ) {
                    Icon(Icons.Outlined.Info, contentDescription = "")
                }
            }
        }
    ) {

        LaunchedEffect(effect) {
            effect?.let {effect ->
                when(effect) {
                    CardDetailEffect.ClosePage -> {
                        navigator.navigateUp()
                    }

                    is CardDetailEffect.ShowSnackbar -> {
                        snackbarHostState.showSnackbar(message = effect.message)
                    }
                }
                viewModel.resetEffect()
            }
        }

        Box (
            Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            if (!state.isScreenLoading) {
                Log.d(TAG, "CardInfoScreen: fields: ${state.fields}")
                CardRender(
                    fields = state.fields,
                    size = state.cardSize.value,
                    color = state.cardColor
                )
            } else {
                Text(text = stringResource(R.string.txt_loading))
            }
        }
    }
}

@Composable
fun CardDetailDeleteDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    DialogComponent(
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        confirmText = R.string.txt_delete,
    ) {
        Text(text = stringResource(R.string.txt_confirm_delete))
    }
}

@Composable
fun CardDetailShareDialog(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    id: String,
) {

    val TAG = "CardDetailShareDialog"

    DialogComponent(
        onConfirm= onConfirm,
        onDismiss = onDismiss,
        confirmText = R.string.txt_close,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(300.dp),
                bitmap = BarcodeEncoder()
                    .run {
                        encodeBitmap(
                            id,
                            BarcodeFormat.QR_CODE,
                            400,
                            400
                        ).asImageBitmap()
                    },
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CardDetailShareDialogPreview() {
    BusyCardAppTheme {
        CardDetailShareDialog(
            onConfirm = {},
            onDismiss = {},
            id = "123456789"
        )
    }
}


@Composable
fun CardRender(
    modifier: Modifier = Modifier,
    fields: List<Field>,
    size: Int,
    color: CardColor
) {
    Surface (
        modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ){
        Box (
            Modifier
                .height(size.dp)
                .background(color = Color(color.color))
        ){
            fields.forEach { field ->
                Box (
                    Modifier.offset {
                        IntOffset(
                            field.offsetX,
                            field.offsetY,
                        )
                    }
                ){
                    when (field) {
                        is Field.AddressField -> CardInfoAddressField(field = field)
                        is Field.ImageField -> CardInfoImageField(field = field)
                        is Field.TextField -> CardInfoTextField(field = field)
                    }
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardInfoBottomSheet(
    modifier: Modifier = Modifier,
    onAddToSharedCards: () -> Unit,
    onDeleteFromSharedCards: () -> Unit,
    onDismissModalSheet: () -> Unit,
    cardState: CardState,
    isBottomSheetLoading: Boolean,
    imagePath: String,
    fields: List<Field>,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismissModalSheet,
        sheetState = sheetState,
    ) {
        if (imagePath.isNotBlank()) {
            val painter = rememberAsyncImagePainter(
                imagePath
            )
            Box (
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape) // TODO: Change to param
                        .size(150.dp)
                )
            }
        } else {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "No Image Available"
            )
        }

//        if (cardState != CardState.MINE) {
//            Row (
//                modifier = Modifier.align(Alignment.CenterHorizontally),
//                verticalAlignment = Alignment.CenterVertically,
//            ) {
//                TextButton(onClick = {
//                    if (cardState != CardState.SHARED) {
//                        onAddToSharedCards()
//                    } else {
//                        onDeleteFromSharedCards()
//                    }
//                }) {
//                    if (cardState != CardState.SHARED) {
//                        Text(stringResource(R.string.txt_add_shared_cards))
//                    } else {
//                        Text(stringResource(R.string.txt_delete_shared_cards))
//                    }
//                }
//                if(isBottomSheetLoading){
//                    CircularProgressIndicator(
//                        modifier = Modifier.size(25.dp)
//                    )
//                }
//            }
//        }

        LazyColumn {
            items(fields) {
                Row (
                    modifier = Modifier.padding(8.dp)
                ) {
                    when (it) {
                        is Field.AddressField -> CompactAddressFieldComponent(
                            textLocalization = it.textLocalization,
                            localization = it.localization,
                        )
                        is Field.TextField -> CompactTextFieldComponent(
                            fieldText = it.value,
                            textType = it.textType,
                        )
                        else -> {}
                    }
                }
            }
        }

        Spacer(Modifier.size(48.dp))
    }
}


@Preview
@Composable
private fun CardRenderPreview() {
    val a = listOf(
        Field.TextField(value = "Print"),
//        Field.TextField(value = "Print", offsetX = 23.0F, offsetY = 100F),
//        Field.TextField(value = "Print", offsetX = 223.0F, offsetY = 400F),
    )

    BusyCardAppTheme {
        CardRender(fields = a, size = 200, color = CardColor.DarkGray)
    }
}
