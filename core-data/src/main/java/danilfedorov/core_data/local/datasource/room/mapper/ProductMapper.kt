package danilfedorov.core_data.local.datasource.room.mapper

import danilfedorov.core_data.local.datasource.room.entity.ProductEntity
import danilfedorov.domain_core.model.Product

class ProductMapper {
    fun mapToEntity(model: Product): ProductEntity {
        return ProductEntity(
            id = model.roomId,
            listId = model.listId,
            firebaseId = model.firebaseId,
            name = model.name,
            isSelected = model.isSelected,
            amount = model.amount,
            unit = model.unit,
        )
    }

    fun mapToModel(entity: ProductEntity): Product {
        return Product(
            roomId = entity.id,
            listId = entity.listId,
            firebaseId = entity.firebaseId,
            name = entity.name,
            isSelected = entity.isSelected,
            amount = entity.amount,
            unit = entity.unit
        )
    }

    fun mapToModels(entities: List<ProductEntity>): List<Product> {
        return entities.map { mapToModel(it) }
    }

    fun mapToEntities(models: List<Product>): List<ProductEntity> {
        return models.map { mapToEntity(it) }
    }
}