package danilfedorov.domain_core.usecase.suggestion

import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.repository.ProductRepository

class InsertProductsFromNamesUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(
        localListId: Long,
        remoteListId: String?,
        rawNames: List<String>
    ) {
        val names = rawNames.map { it.trim() }.filter { it.isNotBlank() }.distinct()
        if (names.isEmpty()) return

        val products = names.map { name ->
            Product(listId = localListId, name = name, isSelected = false)
        }

        products.forEach { product ->
            productRepository.addProduct(remoteListId, product)
        }
    }
}