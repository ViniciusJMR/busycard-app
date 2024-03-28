package dev.vinicius.busycardapp.data.repository.impl

import dev.vinicius.busycardapp.data.repository.Repository
import dev.vinicius.busycardapp.domain.model.card.Card
import javax.inject.Inject
import javax.inject.Singleton

class CardRepository @Inject constructor(

): Repository<Long, Card> {

    // Temp solution
    val cards: MutableList<Card> = ArrayList()

    override suspend fun getById(id: Long): Card {
        // TODO: Remove this!!
        return cards.find {
            it.id == id
        }!!
    }

    override suspend fun save(item: Card) {
        cards.add(item)
    }

}