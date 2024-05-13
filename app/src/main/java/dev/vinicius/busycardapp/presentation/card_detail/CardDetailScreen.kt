package dev.vinicius.busycardapp.presentation.card_detail

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.vinicius.busycardapp.R
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.TextType
import dev.vinicius.busycardapp.presentation.card_detail.component.DialogComponent
import dev.vinicius.busycardapp.ui.theme.BusyCardAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph
@Destination
@Composable
fun CardInfoScreen(
    modifier: Modifier = Modifier,
    viewModel: CardDetailViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    id: String,
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState()
    val event = viewModel::onEvent
    val TAG = "CardInfoScreen"

    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(effect) {
        effect?.let {
            when(it) {
                CardDetailEffect.ClosePage -> {
                    navigator.navigateUp()
                }
            }
            viewModel.resetEffect()
        }
    }

    if (state.showShareDialog) {
        CardDetailShareDialog(
            onConfirm = { event(CardInfoEvent.DialogEvent.OnDismissShareDialog) },
            onDismiss = { event(CardInfoEvent.DialogEvent.OnDismissShareDialog) },
            id = id,
        )
    }

    if (state.showBottomSheet) {
        CardInfoBottomSheet(
            isSharedCard = state.isSharedCard,
            isMyCard = state.isMyCard,
            isBottomSheetLoading = state.isBottomSheetLoading,
            onAddToSharedCards = { event(CardInfoEvent.CardEvent.OnSaveToSharedCard) },
            onDeleteFromSharedCards = { event(CardInfoEvent.CardEvent.OnDeleteFromSharedCard) },
            onDismissModalSheet = { event(CardInfoEvent.ModalEvent.OnDismissModalSheet) },
        )
    }


    Scaffold(
        Modifier.fillMaxSize(),
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
            )
        },
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = {
                        event( CardInfoEvent.DialogEvent.OnShowShareDialog )
                    }
                ) {
                    Icon(Icons.Outlined.Share, contentDescription = "")
                }
                Spacer(modifier = Modifier.height(8.dp))
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
        Box (
            Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            if (!state.isScreenLoading) {
                Log.d(TAG, "CardInfoScreen: fields: ${state.fields}")
                CardRender(fields = state.fields, size = 200)
            } else {
                Text(text = "Loading")
            }
        }
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
            Spacer(modifier = Modifier.height(8.dp))
            Column (
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Outlined.Link, contentDescription = "")
                Text(text = stringResource(R.string.txt_share_as_link))
            }
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
    size: Int
) {
    Surface (
        modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ){
        Box (
            Modifier
                .height(size.dp)
                .background(color = Color.DarkGray)
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

@Composable
fun CardInfoTextField(
    modifier: Modifier = Modifier,
    field: Field.TextField,
) {
    val TAG = "CardInfoTextField"
    var text by remember { mutableIntStateOf(R.string.txt_dial_number) }
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    val onCallIntent : Intent? by remember { mutableStateOf(
        when (field.textType) {
            TextType.PHONE -> {
                text = R.string.txt_dial_number
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:${field.value}"))
            }
            TextType.EMAIL -> {
                text = R.string.txt_send_email_to
                Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${field.value}"))
            }
            else -> { null }
        }
    )}

    var showDialog by remember { mutableStateOf(false) }
    var copied by remember { mutableStateOf(false) }

    if (showDialog) {
        DialogComponent(
            onDismiss = { showDialog = false },
            onConfirm = { showDialog = false },
            confirmText = R.string.txt_close,
        ) {
            Column {
                onCallIntent?.let {
                    TextButton(
                        onClick = { startActivity(context, it, null) }
                    ) {
                        Text(text = stringResource(text))
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                }
                TextButton(
                    onClick = {
                        clipboard.setText(AnnotatedString(field.value))
                        copied = true
                    }
                ) {
                    Text(
                        stringResource(
                            if (!copied) R.string.txt_copy else R.string.txt_copied
                        )
                    )
                }
            }
        }
    }
    Text(
        modifier = Modifier.clickable {
            showDialog = true
        },
        text = field.value
    )
}

@Composable
fun CardInfoImageField(
    modifier: Modifier = Modifier,
    field: Field.ImageField,
) {
    Box( contentAlignment = Alignment.Center ) {
        // Only used to signalize to the user there's a image there
        // TODO: Use onState from AsyncImage
        CircularProgressIndicator()
        AsyncImage(
            model = field.image.path,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(CircleShape) // TODO: Change to param
                .size(field.size.dp)
        )
    }
}

@Composable
fun CardInfoAddressField(
    modifier: Modifier = Modifier,
    field: Field.AddressField,
) {
    val TAG = "CardInfoAddressField"
    Log.d(TAG, "field: ${field.localization}")
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    val onCallIntent: (String) -> Unit = { uri ->
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(context, intent, null)
    }

    var showDialog by remember { mutableStateOf(false) }
    var copied by remember { mutableStateOf(false) }

    if (showDialog) {
        DialogComponent(
            onDismiss = { showDialog = false },
            onConfirm = { showDialog = false },
            confirmText = R.string.txt_close,
        ) {
            Column {
                TextButton(
                    onClick = {
                        onCallIntent("geo:0,0?q=${Uri.encode(field.textLocalization)}")
                    }
                ) {
                    Text("Search text on map")
                }
                Spacer(modifier = Modifier.height(2.dp))
                TextButton(
                    onClick = {
                        field.localization?.let {
                            onCallIntent("geo:${it.latitude},${it.longitude}")
                        }
                    },
                    enabled = field.localization != null
                ) {
                    Text(if (field.localization != null) "Search location on map" else "No location available")
                }
                Spacer(modifier = Modifier.height(2.dp))
                TextButton(
                    onClick = {
                        clipboard.setText(AnnotatedString(field.textLocalization))
                        copied = true
                    }
                ) {
                    Text(
                        stringResource(
                            if (!copied) R.string.txt_copy else R.string.txt_copied
                        )
                    )
                }
            }
        }
    }

    Row (
        modifier = Modifier.clickable {
            showDialog = true
        }
    ) {
        Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null)
        Text(text = field.textLocalization)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardInfoBottomSheet(
    modifier: Modifier = Modifier,
    onAddToSharedCards: () -> Unit,
    onDeleteFromSharedCards: () -> Unit,
    onDismissModalSheet: () -> Unit,
    isSharedCard: Boolean,
    isMyCard: Boolean,
    isBottomSheetLoading: Boolean,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismissModalSheet,
        sheetState = sheetState,
    ) {
        if (!isMyCard) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = {
                    if (!isSharedCard) {
                        onAddToSharedCards()
                    } else {
                        onDeleteFromSharedCards()
                    }
                }) {
                    if (!isSharedCard) {
                        Text("Add to Shared Cards")
                    } else {
                        Text("Delete from Shared Cards")
                    }
                }
                if(isBottomSheetLoading){
                    CircularProgressIndicator(
                        modifier = Modifier.size(25.dp)
                    )
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
        CardRender(fields = a, size = 200)
    }
}

