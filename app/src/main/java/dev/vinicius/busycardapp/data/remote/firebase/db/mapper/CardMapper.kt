package dev.vinicius.busycardapp.data.remote.firebase.db.mapper

import android.net.Uri
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import dev.vinicius.busycardapp.data.remote.firebase.db.model.FirebaseCardModel
import dev.vinicius.busycardapp.data.remote.firebase.db.model.FirebaseFieldModel
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.model.card.CardImage
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.enums.FieldFont
import dev.vinicius.busycardapp.domain.model.card.enums.LocationIconPosition
import dev.vinicius.busycardapp.domain.model.card.enums.TextType

fun Card.mapToFirebaseModel() =
    FirebaseCardModel(
        id = id,
        name = name,
        owner = owner,
        image = if (image.uri != null) image.uri.toString() else null,
        mainContact = mainContact,
        isDraft = isDraft,
    )

fun FirebaseCardModel.mapToDomainModel(fields: List<Map<String, Any>>) =
    Card(
        id = id,
        name =name ?: "",
        owner = owner ?: "",
        mainContact = mainContact ?: "",
        image = CardImage(
            uri = let {
                Log.d("CardMapper", "image: $image")
                if (image != null) Uri.parse(image) else null
            },
        ),
        fields = fields.map { mapFieldToDomainModel(it) },
        isDraft = isDraft ?: false,
    )


fun Field.mapToFirebaseModel(): Map<String, Any> =
    when(this) {
        is Field.AddressField -> mapOf(
            "type" to "ADDRESS",
            "name" to name,
            "offsetX" to offsetX,
            "offsetY" to offsetY,
            "size" to size,
//            "localization" to mapOf("x" to localization.first, "y" to localization.second),
            "textLocalization" to textLocalization,
        )
        is Field.ImageField -> mapOf(
            "type" to "IMAGE",
            "name" to name,
            "offsetX" to offsetX,
            "offsetY" to offsetY,
            "size" to size,
            "imageUrl" to image.uri.toString(),
        )
        is Field.TextField -> mapOf(
            "type" to "TEXT",
            "name" to name,
            "offsetX" to offsetX,
            "offsetY" to offsetY,
            "size" to size,
            "textType" to textType,
            "value" to value,
        )
    }

fun mapDomainFieldsToFirebaseModel(items: List<Field>): List<Map<String, Any>> =
    items.map {
        it.run {
            when(this) {
                is Field.AddressField -> mapOf(
                    "type" to "ADDRESS",
                    "name" to name,
                    "offsetX" to offsetX.toDouble(),
                    "offsetY" to offsetY.toDouble(),
                    "size" to size,
                    "localization" to mapOf("lat" to localization?.latitude, "lng" to localization?.longitude),
                    "textLocalization" to textLocalization,
                    "font" to font,
                    "iconSize" to iconSize,
                    "iconPosition" to iconPosition,
                )
                is Field.ImageField -> mapOf(
                    "type" to "IMAGE",
                    "name" to name,
                    "offsetX" to offsetX.toDouble(),
                    "offsetY" to offsetY.toDouble(),
                    "size" to size,
                    "imageUrl" to image.uri.toString(),
                )
                is Field.TextField -> mapOf(
                    "type" to "TEXT",
                    "name" to name,
                    "offsetX" to offsetX.toDouble(),
                    "offsetY" to offsetY.toDouble(),
                    "size" to size,
                    "textType" to textType,
                    "value" to value,
                    "font" to font,
                )
            }
        }
    }


fun mapFieldToDomainModel(item: Map<String, Any>): Field {
    val offsetX = item["offsetX"].toString().toDouble().toInt()
    val offsetY = item["offsetY"].toString().toDouble().toInt()
    val size = item["size"].toString().toDouble().toInt()
    val iconSize = (item["iconSize"] ?: 0L) as Long

    var latLng: LatLng? = null
    (item["localization"] as? Map<String, Double>)?.let {
        val lat = it["lat"]
        val lng = it["lng"]

        if (lat != null && lng != null) {
            latLng = LatLng(lat, lng)
        }
    }

    return when(item["type"] as String) {
        "ADDRESS" -> Field.AddressField(
            item["name"] as String,
            offsetX,
            offsetY,
            size,
            latLng,
            item["textLocalization"] as String,
            FieldFont.valueOf((item["font"] ?: "SANS_SERIF") as String),
            iconSize.toInt(),
            LocationIconPosition.valueOf((item["iconPosition"] ?: "LEFT") as String),
        )
        "IMAGE" -> Field.ImageField(
            item["name"] as String,
            offsetX,
            offsetY,
            size,
            CardImage(
                uri = Uri.parse(item["imageUrl"] as String),
                path = item["imageUrl"] as String
            ),
        )
        "TEXT" -> Field.TextField(
            item["name"] as String,
            offsetX,
            offsetY,
            size,
            TextType.valueOf(item["textType"] as String),
            item["value"] as String,
            FieldFont.valueOf((item["font"] ?: "SANS_SERIF") as String)
        )

        else -> {
            throw MapperException("Error when mapping fields: Wrong type ${item["type"] as String}")
        }
    }
}

fun FirebaseFieldModel.mapToDomainModel() =
    when(this) {
        is FirebaseFieldModel.AddressField -> Field.AddressField(
            name ?: "",
            offsetX ?: 0,
            offsetY ?: 0,
            size ?: 0,
//            localization ?: Pair(0,0),
//            textLocalization ?: "",
        )
        is FirebaseFieldModel.ImageField -> Field.ImageField(
            name ?: "",
            offsetX ?: 0,
            offsetY ?: 0,
            size ?: 0,
//            imageUrl ?: "",
        )
        is FirebaseFieldModel.TextField -> Field.TextField(
            name ?: "",
            offsetX ?: 0,
            offsetY ?: 0,
            size ?: 0,
            textType ?: TextType.TEXT,
            value ?: "",
        )
    }


