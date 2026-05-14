package cod.majestic.discord.bot.service.button.impl

import cod.majestic.discord.bot.service.button.CreateApplicationGetRangService
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service

@Service
class CreateApplicationGetRangServiceImpl(
    private val jda: JDA
) : CreateApplicationGetRangService, InitializingBean {

    override fun createAndSendButton() {
        val guild = jda.guilds.first()
        val channel = guild.textChannels
            .find { it.name == "\uD83D\uDD2B・заявки-на-получение-ранга" } ?: return

        channel.history.retrievePast(20).queue { messages ->
            val alreadyExists = messages.any { message ->
                isBot(message.author.id, jda.selfUser.id)
            }

            if (!alreadyExists) {
                val button = Button.primary(
                    "create_application_get_rang",
                    "Отправить заявку на получения ранга"
                )

                channel.sendMessage("Нажмите ниже чтобы отправить заявку хай-составу и получить ранг.")
                    .setActionRow(button)
                    .queue()
            }
        }
    }

    fun isBot(messageUserId: String, botUserId: String) = messageUserId == botUserId

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}