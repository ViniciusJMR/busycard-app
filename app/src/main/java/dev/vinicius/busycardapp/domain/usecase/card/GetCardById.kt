package dev.vinicius.busycardapp.domain.usecase.card

import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.domain.repository.IRepository
import dev.vinicius.busycardapp.domain.model.card.Card
import javax.inject.Inject

class GetCardById @Inject constructor(
    private val repository: IRepository<String, Card>
): UseCase<String, Card>() {
    override suspend fun execute(param: String) = repository.getById(param)
}