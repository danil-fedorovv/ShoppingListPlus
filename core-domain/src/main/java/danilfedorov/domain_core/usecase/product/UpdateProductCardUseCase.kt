package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.repository.ProductRepository

class UpdateProductCardUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(
        remoteListId: String?,
        product: Product,
        name: String = "",
        amount: String = "",
        unit: String = ""
    )  {
        if(listOf(name, amount, unit).all { it.isEmpty()}) return
        if(name.isNotEmpty()) repository.updateProductName(remoteListId, product, name)
        if(amount.isNotEmpty()) repository.updateAmountAndUnit(remoteListId, product, amount, unit)
    }
}