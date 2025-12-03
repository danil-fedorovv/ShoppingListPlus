package danilfedorov.domain_core.exceptions

class RemoteListNotFoundException(
    override val message: String = "No list found for the given Firebase link"
) : Exception(message)

class FirebasePushFailedException(
    override val message: String = "Failed to create shared list"
) : Exception(message)

class FirebaseKeyGenerationException(
    override val message: String = "Failed to generate Firebase push key. This should never happen"
) : Exception(message)

class TimeoutException (
    override val message: String = "Request timed out after 15 seconds"
) : Exception(message)