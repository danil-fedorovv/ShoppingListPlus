package danilfedorov.domain_core.usecase.premium

import danilfedorov.domain_core.repository.PremiumRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GetPremiumExpirationUseCase(
    private val premiumRepository: PremiumRepository
) {
    suspend operator fun invoke(): String? {
        val expiration = premiumRepository.getExpirationDate() ?: return null
        val date = Date(expiration)

        val format = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        val formattedDate = format.format(date)

        return formattedDate
    }
}