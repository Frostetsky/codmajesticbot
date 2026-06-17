package cod.majestic.discord.bot.service.impl

import cod.majestic.discord.bot.model.EventCrime
import cod.majestic.discord.bot.model.projection.CrimeStatisticProjection
import cod.majestic.discord.bot.repository.EventCrimeRepository
import cod.majestic.discord.bot.service.EventCrimeService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class EventCrimeServiceImpl(
    private val eventCrimeRepository: EventCrimeRepository
) : EventCrimeService {

    override fun save(eventCrime: EventCrime): EventCrime = eventCrimeRepository.save(eventCrime)

    override fun findById(id: UUID): EventCrime = eventCrimeRepository.findById(id).orElseThrow()

    override fun findAllNotOlderThanDays(limit: Int, days: Long): Page<CrimeStatisticProjection>? {
        val threshold = LocalDateTime.now().minusDays(days)
        val pageable = PageRequest.of(0, limit)
        return null
    }
}