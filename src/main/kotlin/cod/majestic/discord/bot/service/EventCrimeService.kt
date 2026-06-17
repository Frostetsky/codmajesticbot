package cod.majestic.discord.bot.service

import cod.majestic.discord.bot.model.EventCrime
import cod.majestic.discord.bot.model.projection.CrimeStatisticProjection
import org.springframework.data.domain.Page
import java.util.UUID

interface EventCrimeService {

    fun save(eventCrime: EventCrime): EventCrime

    fun findById(id: UUID): EventCrime

    fun findAllNotOlderThanDays(limit: Int, days: Long): Page<CrimeStatisticProjection>?
}