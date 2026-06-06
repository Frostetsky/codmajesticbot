package cod.majestic.discord.bot.service.impl

import cod.majestic.discord.bot.model.enums.EventCrimeType
import cod.majestic.discord.bot.repository.FamilyMemberRepository
import cod.majestic.discord.bot.service.StatisticService
import org.springframework.stereotype.Service

@Service
class StatisticServiceImpl(
    private val familyMemberRepository: FamilyMemberRepository
) : StatisticService {

    override fun getCountByCrimeEvent(eventCrimeType: EventCrimeType) {
        TODO("Not yet implemented")
    }
}