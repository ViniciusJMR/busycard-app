package dev.vinicius.busycardapp.domain.usecase.card

import dev.vinicius.busycardapp.core.UseCase
import dev.vinicius.busycardapp.domain.repository.IRepository
import dev.vinicius.busycardapp.domain.model.card.Card
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveCard @Inject constructor(
    private val repository: IRepository<String, Card>,
): UseCase.NoSource<Card>() {
    override suspend fun execute(param: Card) = flow {
        // Validation

        // Change owner to logged user

        // Save
        param.owner = "Vinicius"
        emit(repository.save(param))
    }
}