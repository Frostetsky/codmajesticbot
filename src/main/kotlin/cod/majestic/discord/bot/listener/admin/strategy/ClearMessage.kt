package cod.majestic.discord.bot.listener.admin.strategy

import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.dto.ClearMessageRequest
import cod.majestic.discord.bot.model.enums.AdminCommand
import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.server.CodFamilyServer
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class ClearMessage(
    private val discordChannelCache: DiscordChannelCache,
    private val codFamilyServer: CodFamilyServer
) : CommandHandler<ClearMessageRequest> {

    override fun execute(body: ClearMessageRequest): Pair<TextChannel, String> {
        val channel = codFamilyServer.getMyGuild().getTextChannelById(body.channelId)
            ?: return Pair(replyChannel(), "❌ Текстовый канал с id ${body.channelId} не найден.")

        // Один и тот же MessageHistory листается вглубь, поэтому повторные retrievePast не дублируют сообщения.
        val history = channel.history
        var remaining = body.count
        var deleted = 0

        while (remaining > 0) {
            val batch = history.retrievePast(minOf(remaining, 100)).complete()
            if (batch.isEmpty()) break

            // purgeMessages сама делит на bulk-удаление (до 14 дней) и удаление по одному (старше 14 дней).
            CompletableFuture.allOf(*channel.purgeMessages(batch).toTypedArray()).join()

            deleted += batch.size
            remaining -= batch.size
        }

        return Pair(replyChannel(), "🧹 Удалено сообщений: **$deleted** в канале ${channel.asMention}.")
    }

    override fun command(): AdminCommand = AdminCommand.CLEAR_MESSAGE

    override fun replyChannel(): TextChannel = discordChannelCache.getBy(MainChannelType.ADMINISTRATION)
}
