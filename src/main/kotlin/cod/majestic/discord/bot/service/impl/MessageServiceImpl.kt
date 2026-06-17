package cod.majestic.discord.bot.service.impl

import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.cache.DiscordRoleCache
import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.service.MessageService
import net.dv8tion.jda.api.entities.Message
import org.springframework.stereotype.Service

@Service
class MessageServiceImpl(
    private val discordChannelCache: DiscordChannelCache
) : MessageService {

    override fun getMessageFromActivityMemberFamilyChannel(id: Long): Message =
        discordChannelCache.getBy(MainChannelType.ACTIVITY_FAMILY_MEMBER_HISTORY)
            .retrieveMessageById(id).complete()
}