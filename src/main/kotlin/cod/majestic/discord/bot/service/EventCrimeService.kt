package cod.majestic.discord.bot.service

import cod.majestic.discord.bot.model.EventCrime
import java.util.UUID

interface EventCrimeService {

    fun save(eventCrime: EventCrime): EventCrime

    fun findById(id: UUID): EventCrime
}