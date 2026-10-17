package danilfedorov.domain_core.usecase.shoppinglist

import danilfedorov.domain_core.exceptions.RemoteListNotFoundException
import danilfedorov.domain_core.exceptions.TimeoutException
import danilfedorov.domain_core.model.firebase.GetRemoteListNameResult
import danilfedorov.domain_core.repository.ShoppingListRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetRemoteShoppingListNameUseCase @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke(remoteListId: String): GetRemoteListNameResult = runCatching {
        coroutineScope {
            val nameDeferred = async { shoppingListRepository.getRemoteListName(remoteListId) }

            val localIdDeferred = async { shoppingListRepository.getLocalListIdByRemoteId(remoteListId) }

            val name = nameDeferred.await()
            val localId = localIdDeferred.await()

            if (localId != null) {
                return@coroutineScope GetRemoteListNameResult.AlreadyJoined(name, localId)
            } else {
                GetRemoteListNameResult.Success(name)
            }
        }
    }.getOrElse { exception ->
        when (exception) {
            is TimeoutException -> GetRemoteListNameResult.Timeout
            is RemoteListNotFoundException -> GetRemoteListNameResult.NotFound
            else -> GetRemoteListNameResult.Error
        }
    }
}