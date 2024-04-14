package dev.vinicius.busycardapp.domain.usecase.card

import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.data.repository.Repository
import dev.vinicius.busycardapp.domain.model.card.Card
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCardById @Inject constructor(
    private val repository: Repository<String, Card>
): UseCase<String, Card>() {
    override suspend fun execute(param: String) = repository.getById(param)
}