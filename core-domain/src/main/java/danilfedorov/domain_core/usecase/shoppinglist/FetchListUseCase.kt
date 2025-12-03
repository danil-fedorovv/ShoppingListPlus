package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.exceptions.RemoteListNotFoundException
import danilfedorov.domain_core.exceptions.TimeoutException
import danilfedorov.domain_core.model.firebase.FetchRemoteListResult
import danilfedorov.domain_core.repository.ProductRepository
import danilfedorov.domain_core.repository.ShoppingListRepository

class FetchListUseCase(
    private val shoppingListRepository: ShoppingListRepository,
    private val productRepository: ProductRepository,
) {

    suspend operator fun invoke(remoteListId: String): FetchRemoteListResult {

        return runCatching {
            val (remoteList, remoteProducts) = shoppingListRepository.fetchList(remoteListId)

            val newListId = shoppingListRepository.createShoppingList(
                remoteList.copy(remoteListId = remoteListId)
            )

            val productsWithLocalId = remoteProducts.map { it.copy(listId = newListId) }

            productRepository.addProducts(productsWithLocalId)

            FetchRemoteListResult.Success(newListId)
        }.getOrElse { error ->
            when (error) {
                is RemoteListNotFoundException -> {
                    FetchRemoteListResult.RemoteListNotFound
                }
                is TimeoutException -> {
                    FetchRemoteListResult.Timeout
                }
                else -> FetchRemoteListResult.Error(Exception(error))
            }
        }
    }
}