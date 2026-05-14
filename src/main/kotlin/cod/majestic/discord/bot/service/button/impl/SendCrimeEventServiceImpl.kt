package cod.majestic.discord.bot.service.button.impl

import cod.majestic.discord.bot.builder.EventCrimeMenuBuilder
import cod.majestic.discord.bot.service.button.SendCrimeEventService
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service

@Service
class SendCrimeEventServiceImpl(
    private val jda: JDA
) : SendCrimeEventService, InitializingBean {

    override fun createAndSendButton() {
        val guild = jda.guilds.first()
        val channel = guild.textChannels
            .find { it.name.contains("└・активность-и-баллы⚔\uFE0F") } ?: return

        channel.history.retrievePast(20).queue { messages ->
            val alreadyExists = messages.any { message -> isBot(message.author.id, jda.selfUser.id) }

            if (!alreadyExists) {
                val selectMenu = EventCrimeMenuBuilder.get()
                channel.sendMessage(
                    """
                    Нажмите ниже чтобы отправить информацию об участии в семейном событии.
                    Укажите сервер, приложите ссылку.
                    """.trimIndent()
                )
                    .setActionRow(selectMenu)
                    .queue()
            }
        }
    }

    fun isBot(messageUserId: String, botUserId: String) = messageUserId == botUserId

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}