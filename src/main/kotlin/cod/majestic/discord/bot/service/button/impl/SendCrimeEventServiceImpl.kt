package cod.majestic.discord.bot.service.button.impl

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
            .find { it.name.contains("\uD83C\uDFEE・откаты") } ?: return

        channel.history.retrievePast(20).queue { messages ->
            val alreadyExists = messages.any { message ->
                isBot(message.author.id, jda.selfUser.id) && existMessage(message.contentRaw)
            }

            if (!alreadyExists) {
                val button = Button.danger(
                    "send_crime_event",
                    "Отправить откат (MCL, VZZ, CAPT, GG, RP, VZM)"
                )

                channel.sendMessage("Нажмите ниже чтобы отправить откат. Укажите категорию, сервер, приложите ссылку.")
                    .setActionRow(button)
                    .queue()
            }
        }
    }

    fun isBot(messageUserId: String, botUserId: String) = messageUserId == botUserId

    fun existMessage(message: String) = message.contains(
        "Нажмите ниже чтобы отправить откат. Укажите категорию, дату и время, приложите ссылку."
    )

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}