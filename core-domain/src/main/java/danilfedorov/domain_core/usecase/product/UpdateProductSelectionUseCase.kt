package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.repository.ProductRepository


class UpdateProductSelectionUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(id: Long, isSelected: Boolean) =
        repository.updateProductSelection(id, isSelected)
}