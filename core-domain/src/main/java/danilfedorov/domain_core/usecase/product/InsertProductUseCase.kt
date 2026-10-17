package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.repository.ProductRepository
import javax.inject.Inject

class InsertProductUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke(remoteListId: String?, item: Product): Long =
        repository.addProduct(remoteListId, item)
}