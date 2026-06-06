package cod.majestic.discord.bot.service

import cod.majestic.discord.bot.model.enums.EventCrimeType

interface StatisticService {

    fun getCountByCrimeEvent(eventCrimeType: EventCrimeType)
}