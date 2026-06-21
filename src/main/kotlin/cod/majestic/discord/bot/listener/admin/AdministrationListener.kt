package cod.majestic.discord.bot.listener.admin

import cod.majestic.discord.bot.listener.admin.strategy.CommandDispatcher
import cod.majestic.discord.bot.model.enums.MainChannelType
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class AdministrationListener(
    private val commandDispatcher: CommandDispatcher
) : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.channel.name != MainChannelType.ADMINISTRATION.nameInDiscord) return

        val reply = commandDispatcher.execute(event)

        sendReply(reply)
    }

    private fun sendReply(pair: Pair<TextChannel, String>) {
        pair.first.sendMessage(pair.second).complete()
    }
}