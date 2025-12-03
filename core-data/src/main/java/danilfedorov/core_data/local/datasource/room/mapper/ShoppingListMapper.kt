package danilfedorov.core_data.local.datasource.room.mapper

import danilfedorov.core_data.local.datasource.room.entity.ShoppingListEntity
import danilfedorov.domain_core.model.ShoppingList

class ShoppingListMapper {
    fun mapToEntity(model: ShoppingList): ShoppingListEntity {
        return ShoppingListEntity(
            id = model.id,
            name = model.name,
            isSortedByAlphabet = model.isSortedByAlphabet,
            deletedAt = model.deletedAt,
            isArchived = model.isArchived,
            remoteListId = model.remoteListId
        )
    }
    fun mapToModel(entity: ShoppingListEntity): ShoppingList {
        return ShoppingList(
            id = entity.id,
            name = entity.name,
            isSortedByAlphabet = entity.isSortedByAlphabet,
            deletedAt = entity.deletedAt,
            isArchived = entity.isArchived,
            remoteListId = entity.remoteListId
        )
    }
    fun mapToModels(entities: List<ShoppingListEntity>): List<ShoppingList> {
        return entities.map { mapToModel(it) }
    }
}
