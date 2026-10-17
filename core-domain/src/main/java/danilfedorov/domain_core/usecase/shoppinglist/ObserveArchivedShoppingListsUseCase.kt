package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveArchivedShoppingListsUseCase @Inject constructor(private val repository: ShoppingListRepository) {
    operator fun invoke(): Flow<List<ShoppingList>> =
        repository.observeArchivedShoppingLists()
}