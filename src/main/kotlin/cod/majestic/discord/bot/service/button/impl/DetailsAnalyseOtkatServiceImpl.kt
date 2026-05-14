package cod.majestic.discord.bot.service.button.impl

import cod.majestic.discord.bot.service.button.DetailsAnalyseOtkatService
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service

@Service
class DetailsAnalyseOtkatServiceImpl(
    private val jda: JDA
) : DetailsAnalyseOtkatService, InitializingBean {


    override fun createAndSendButton() {
        val guild = jda.guilds.first()
        val channel = guild.textChannels
            .find { it.name == "\uD83C\uDD7E\uFE0F・заявки-на-разбор-отката" } ?: return

        channel.history.retrievePast(20).queue { messages ->
            val alreadyExists = messages.any { message ->
                isBot(message.author.id, jda.selfUser.id)
            }

            if (!alreadyExists) {
                val button = Button.primary(
                    "details_analyse_otkat",
                    "Запросить подробный разгор отката с хай-составом"
                )

                channel.sendMessage("Нажмите ниже чтобы отправить заявку на подробный разбор отката.")
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