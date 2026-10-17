package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.repository.ProductRepository
import javax.inject.Inject

class InsertProductsUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke(itemList: List<Product>) = repository.addProducts(itemList)
}