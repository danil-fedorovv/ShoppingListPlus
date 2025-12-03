package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow

class ObserveArchivedShoppingListsUseCase(private val repository: ShoppingListRepository) {
    operator fun invoke(): Flow<List<ShoppingList>> =
        repository.observeArchivedShoppingLists()
}