package danilfedorov.domain_core.usecase.product

import danilfedorov.domain_core.repository.ProductRepository

class UpdateProductCardUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(
        id: Long,
        name: String = "",
        amount: String = "",
        unit: String = ""
    )  {
        if(listOf(name, amount, unit).all { it.isEmpty()}) return
        if(name.isNotEmpty()) repository.updateProductName(id, name)
        if(amount.isNotEmpty()) repository.updateAmountAndUnit(id, amount, unit)
    }
}