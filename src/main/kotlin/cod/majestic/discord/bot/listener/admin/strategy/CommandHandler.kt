package cod.majestic.discord.bot.listener.admin.strategy

import cod.majestic.discord.bot.model.enums.AdminCommand
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.apache.commons.lang3.StringUtils

interface CommandHandler<RQ> {

    fun execute(body: RQ): Pair<TextChannel, String>

    fun command(): AdminCommand

    fun replyChannel(): TextChannel

    fun replyMessage(): String? = StringUtils.EMPTY

    fun parseAsMentionToUserId(asMention: String): Long {
        val mentionPattern = Regex("^<@!?(\\d+)>$")
        return mentionPattern.find(asMention)
            ?.groupValues
            ?.get(1)
            ?.toLongOrNull()
            ?: throw IllegalArgumentException("Invalid mention format: $asMention")
    }
}