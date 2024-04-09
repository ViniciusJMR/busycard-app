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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

class CardRepository @Inject constructor(
): Repository<Long, Card> {

    // TODO: move it to interface
    private val database = Firebase.database.reference

    // Temp solution
    val cards: MutableList<Card> = ArrayList()

    override suspend fun getById(id: Long) = flow {
        // TODO: Remove this!!
        val card: Card
        val dataSnapshot = database.child("cards").child(id.toString()).get()
        dataSnapshot.await()

        when {
            dataSnapshot.isSuccessful -> {
                val firebaseCard = dataSnapshot.result.getValue<FirebaseCardModel>()
                card = firebaseCard!!.mapToDomainModel()
                emit(card)
            }
            dataSnapshot.isCanceled -> {
                TODO()
            }
        }
    }

    override suspend fun save(item: Card) {
        database.child("cards").child(item.id.toString()).setValue(item.mapToFirebaseModel())
    }

}