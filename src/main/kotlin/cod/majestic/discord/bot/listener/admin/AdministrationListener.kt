package cod.majestic.discord.bot.listener.admin

import cod.majestic.discord.bot.model.enums.MainChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class AdministrationListener : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.channel.name != MainChannelType.ADMINISTRATION.nameInDiscord) return


    }
}