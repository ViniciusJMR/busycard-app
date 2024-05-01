package dev.vinicius.busycardapp.data.remote.firebase.db.mapper

import dev.vinicius.busycardapp.data.remote.firebase.db.model.FirebaseCardModel
import dev.vinicius.busycardapp.data.remote.firebase.db.model.FirebaseFieldModel
import dev.vinicius.busycardapp.domain.model.card.Card
import dev.vinicius.busycardapp.domain.model.card.CardImage
import dev.vinicius.busycardapp.domain.model.card.Field
import dev.vinicius.busycardapp.domain.model.card.TextType

fun Card.mapToFirebaseModel() =
    FirebaseCardModel(
        id,
        name,
        owner,
        image.uri.toString(),
        mainContact,
    )

fun FirebaseCardModel.mapToDomainModel(fields: List<Map<String, Any>>) =
    Card(
        id,
        name ?: "",
        owner ?: "",
        mainContact ?: "",
        CardImage(path = image ?: ""),
        fields.map { mapFieldToDomainModel(it) }
    )


fun Field.mapToFirebaseModel(): Map<String, Any> =
    when(this) {
        is Field.AddressField -> mapOf(
            "type" to "ADDRESS",
            "name" to name,
            "offsetX" to offsetX,
            "offsetY" to offsetY,
            "size" to size,
            "localization" to mapOf("x" to localization.first, "y" to localization.second),
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
                    "localization" to mapOf("x" to localization.first, "y" to localization.second),
                    "textLocalization" to textLocalization,
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
                )
            }
        }
    }


fun mapFieldToDomainModel(item: Map<String, Any>): Field {
    val offsetX = item["offsetX"].toString().toDouble().toInt()
    val offsetY = item["offsetY"].toString().toDouble().toInt()
    val size = item["size"].toString().toDouble().toInt()


    return when(item["type"] as String) {
        "ADDRESS" -> Field.AddressField(
            item["name"] as String,
            offsetX,
            offsetY,
            size,
            Pair(
                (item["localization"] as Map<String, Long>)["x"],
                (item["localization"] as Map<String, Long>)["y"] //Really ugly solution. Do better in future
            ) as Pair<Long, Long>,
            item["textLocalization"] as String,
        )
        "IMAGE" -> Field.ImageField(
            item["name"] as String,
            offsetX,
            offsetY,
            size,
            CardImage(path = item["imageUrl"] as String),
        )
        "TEXT" -> Field.TextField(
            item["name"] as String,
            offsetX,
            offsetY,
            size,
            TextType.valueOf(item["textType"] as String),
            item["value"] as String,
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
            localization ?: Pair(0,0),
            textLocalization ?: "",
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


