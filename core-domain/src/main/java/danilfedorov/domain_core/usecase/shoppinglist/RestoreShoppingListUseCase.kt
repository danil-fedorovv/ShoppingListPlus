package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.repository.ShoppingListRepository
import javax.inject.Inject

class RestoreShoppingListUseCase @Inject constructor(private val repository: ShoppingListRepository) {
    suspend operator fun invoke(id: Long) = repository.restoreShoppingList(id)
}