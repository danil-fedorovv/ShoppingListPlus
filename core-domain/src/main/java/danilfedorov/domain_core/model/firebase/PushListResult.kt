package danilfedorov.domain_core.model.firebase

sealed class PushListResult {
    data class Success(val remoteId: String) : PushListResult()
    object Timeout: PushListResult()
    object Failure: PushListResult()
}