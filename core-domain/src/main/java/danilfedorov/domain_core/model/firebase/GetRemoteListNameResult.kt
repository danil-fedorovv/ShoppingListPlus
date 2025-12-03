package danilfedorov.domain_core.model.firebase

sealed class GetRemoteListNameResult {
    data class Success(val listName: String) : GetRemoteListNameResult()
    data class AlreadyJoined(val listName: String, val localListId: Long) : GetRemoteListNameResult()
    object Timeout : GetRemoteListNameResult()
    object NotFound : GetRemoteListNameResult()
    object Error : GetRemoteListNameResult()
}