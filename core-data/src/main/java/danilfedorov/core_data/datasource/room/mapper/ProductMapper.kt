package danilfedorov.core_data.datasource.room.mapper

import danilfedorov.core_data.datasource.room.entity.ProductEntity
import danilfedorov.domain_core.model.Product

class ProductMapper {
    fun mapToEntity(model: Product): ProductEntity {
        return ProductEntity(
            id = model.id,
            listId = model.listId,
            name = model.name,
            isSelected = model.isSelected,
            amount = model.amount,
            unit = model.unit,
        )
    }
    fun mapToModel(entity: ProductEntity): Product {
        return Product(
            id = entity.id,
            listId = entity.listId,
            name = entity.name,
            isSelected = entity.isSelected,
            amount = entity.amount,
            unit = entity.unit
        )
    }
    fun mapToModels(entities: List<ProductEntity>): List<Product> {
        return entities.map { mapToModel(it) }
    }
}