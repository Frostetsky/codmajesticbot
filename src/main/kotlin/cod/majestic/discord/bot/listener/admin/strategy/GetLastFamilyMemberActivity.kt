package cod.majestic.discord.bot.listener.admin.strategy

import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.dto.GetLastFamilyRequest
import cod.majestic.discord.bot.mapper.AdministrationMapper
import cod.majestic.discord.bot.model.enums.AdminCommand
import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.service.EventCrimeService
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.springframework.stereotype.Component

@Component
class GetLastFamilyMemberActivity(
    private val eventCrimeService: EventCrimeService,
    private val administrationMapper: AdministrationMapper,
    private val discordChannelCache: DiscordChannelCache
) : CommandHandler<GetLastFamilyRequest> {

    override fun execute(body: GetLastFamilyRequest): Pair<TextChannel, String> {
        val eventCrimesStatistic = eventCrimeService.findAllNotOlderThanDays(body.limit, body.days)!!.toList()
        val message = administrationMapper.toDiscordMessage(eventCrimesStatistic, body.days)
        return Pair(replyChannel(), message)
    }

    override fun command(): AdminCommand = AdminCommand.GET_ACTIVE_OF_MEMBER

    override fun replyChannel(): TextChannel = discordChannelCache.getBy(MainChannelType.ACTIVITY_FAMILY_MEMBER_HISTORY)
}