package dev.vinicius.busycardapp.domain.model.usecase.card

import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.data.repository.Repository
import dev.vinicius.busycardapp.domain.model.card.Card
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCardById @Inject constructor(
    private val repository: Repository<Long, Card>
): UseCase<Long, Card>() {
    override suspend fun execute(param: Long) = repository.getById(param)
}