package cod.majestic.discord.bot.service.button.impl

import cod.majestic.discord.bot.service.button.CreateApplicationInFamilyService
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service

@Service
class CreateApplicationInFamilyServiceImpl(
    private val jda: JDA
) : CreateApplicationInFamilyService, InitializingBean {

    override fun createAndSendButton() {
        val guild = jda.guilds.first()
        val channel = guild.textChannels.find {
            it.name.contains("заявка-в-семью") && it.name.length == 16
        } ?: return

        channel.history.retrievePast(20).queue { messages ->
            val alreadyExists = messages.any { message ->
                isBot(message.author.id, jda.selfUser.id) && existMessage(message.contentRaw)
            }

            if (!alreadyExists) {
                channel.sendMessage("Нажмите кнопку ниже, чтобы создать заявку.")
                    .setActionRow(
                        Button.primary("create_application", "Создать заявку в семью")
                    ).queue()
            }
        }
    }

    fun isBot(messageUserId: String, botUserId: String) = messageUserId == botUserId

    fun existMessage(message: String) = message.contains("Нажмите кнопку ниже, чтобы создать заявку.")

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}