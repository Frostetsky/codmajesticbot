package cod.majestic.discord.bot.listener.admin.strategy

import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.dto.GetInactiveMemberRequest
import cod.majestic.discord.bot.mapper.AdministrationMapper
import cod.majestic.discord.bot.model.enums.AdminCommand
import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.repository.InactiveMemberRepository
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.springframework.stereotype.Component

@Component
class GetInactiveMember(
    private val discordChannelCache: DiscordChannelCache,
    private val inactiveMemberRepository: InactiveMemberRepository,
    private val administrationMapper: AdministrationMapper
) : CommandHandler<GetInactiveMemberRequest> {

    override fun execute(body: GetInactiveMemberRequest): Pair<TextChannel, String> {
        val inactiveMembers = inactiveMemberRepository.findAll()
        return Pair(replyChannel(), administrationMapper.toDiscordMessage(inactiveMembers))
    }

    override fun command(): AdminCommand = AdminCommand.GET_INACTIVE_MEMBER

    override fun replyChannel(): TextChannel = discordChannelCache.getBy(MainChannelType.ADMINISTRATION)
}