package cod.majestic.discord.bot.listener.admin.strategy

import cod.majestic.discord.bot.dto.GetLastFamilyRequest
import cod.majestic.discord.bot.mapper.EventCrimeMapper
import cod.majestic.discord.bot.model.enums.AdminCommand
import cod.majestic.discord.bot.service.EventCrimeService
import org.springframework.stereotype.Component

@Component
class GetLastFamilyMemberActivity(
    private val eventCrimeService: EventCrimeService,
    private val eventCrimeMapper: EventCrimeMapper
) : CommandHandler<GetLastFamilyRequest> {

    override fun execute(body: GetLastFamilyRequest): String {
        val eventCrimesStatistic = eventCrimeService.findAllNotOlderThanDays(body.limit, body.days)!!.toList()
        return eventCrimeMapper.toDiscordMessage(eventCrimesStatistic, body.days)
    }

    override fun command(): AdminCommand = AdminCommand.GET_ACTIVE_OF_MEMBER
}