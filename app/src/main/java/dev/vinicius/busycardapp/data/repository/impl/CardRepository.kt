package dev.vinicius.busycardapp.data.repository.impl

import android.util.Log
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.vinicius.busycardapp.data.remote.firebase.mapper.mapToDomainModel
import dev.vinicius.busycardapp.data.remote.firebase.mapper.mapToFirebaseModel
import dev.vinicius.busycardapp.data.remote.firebase.model.FirebaseCardModel
import dev.vinicius.busycardapp.data.repository.Repository
import dev.vinicius.busycardapp.domain.model.card.Card
import javax.inject.Inject
import javax.inject.Singleton

class CardRepository @Inject constructor(
): Repository<Long, Card> {

    // TODO: move it to interface
    private val database = Firebase.database.reference

    // Temp solution
    val cards: MutableList<Card> = ArrayList()

    override suspend fun getById(id: Long): Card {
        // TODO: Remove this!!
        var card: Card = Card(-1, "invalid", "Me", emptyList())
        database.child("cards").child(id.toString()).get()
            .addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
                val firebaseCard = it.getValue<FirebaseCardModel>()
                Log.d("firebase", "getById: $firebaseCard")
                card = firebaseCard!!.mapToDomainModel()
                Log.d("firebase", "card: $card")
            }
            .addOnFailureListener {
                Log.i("firebase", "Error getting data", it)
            }

        return card
    }

    override suspend fun save(item: Card) {
        database.child("cards").child(item.id.toString()).setValue(item.mapToFirebaseModel())
    }

}