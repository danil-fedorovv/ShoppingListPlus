package danilfedorov.domain_core.model.firebase

sealed class FetchRemoteListResult {
    data class Success(val localListId: Long) : FetchRemoteListResult()
    object RemoteListNotFound : FetchRemoteListResult()
    object Timeout : FetchRemoteListResult()
    data class Error(val exception: Exception) : FetchRemoteListResult()
}