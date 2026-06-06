package cod.majestic.discord.bot.service.impl

import cod.majestic.discord.bot.model.EventCrime
import cod.majestic.discord.bot.repository.EventCrimeRepository
import cod.majestic.discord.bot.service.EventCrimeService
import org.springframework.stereotype.Service
import java.util.*

@Service
class EventCrimeServiceImpl(
    private val eventCrimeRepository: EventCrimeRepository
) : EventCrimeService {

    override fun save(eventCrime: EventCrime): EventCrime = eventCrimeRepository.save(eventCrime)

    override fun findById(id: UUID): EventCrime = eventCrimeRepository.findById(id).orElseThrow()
}