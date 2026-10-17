package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.repository.ProductRepository
import javax.inject.Inject


class UpdateProductSelectionUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke(remoteListId: String?, product: Product, isSelected: Boolean) =
        repository.updateProductSelection(remoteListId, product, isSelected)
}