package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.exceptions.TimeoutException
import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.model.firebase.PushListResult
import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ShoppingListRepository
import javax.inject.Inject

class PushListUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) {
    suspend operator fun invoke(
        list: ShoppingList,
        products: List<Product>
    ): PushListResult = try {
        list.remoteListId?.let { return PushListResult.Success(it) }

        val remoteId = shoppingListRepository.pushList(list, products)
        shoppingListRepository.updateRemoteListId(list.id, remoteId)

        PushListResult.Success(remoteId)
    } catch (e: Exception) {
        when(e) {
            is TimeoutException -> PushListResult.Timeout
            else -> PushListResult.Failure
        }
    }
}