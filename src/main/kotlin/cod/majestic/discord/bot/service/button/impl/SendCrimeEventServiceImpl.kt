package cod.majestic.discord.bot.service.button.impl

import cod.majestic.discord.bot.model.MainButton
import cod.majestic.discord.bot.model.enums.MainButtonType
import cod.majestic.discord.bot.server.CodFamilyServer
import cod.majestic.discord.bot.service.button.SendCrimeEventService
import cod.majestic.discord.bot.service.profile.MainButtonService
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import java.util.*

@Service
class SendCrimeEventServiceImpl(
    private val codFamilyServer: CodFamilyServer,
    private val mainButtonService: MainButtonService
) : SendCrimeEventService, InitializingBean {

    override fun createAndSendButton() {
        val guild = codFamilyServer.getMyGuild()
        val channel = guild.textChannels
            .find { it.name.contains("\uD83C\uDFEE・откаты") } ?: return

        if (!mainButtonService.existsByButton(MainButtonType.SEND_CRIME_EVENT)) {
            val button = Button.danger(
                MainButtonType.SEND_CRIME_EVENT.id,
                "Отправить информацию"
            )

            channel.sendMessage("Нажмите ниже чтобы отправить всю необходимую информацию")
                .setActionRow(button)
                .queue {
                    val messageId = it.idLong
                    val mainButton = MainButton(
                        id = UUID.randomUUID(),
                        discordMessageId = messageId,
                        nameButton = MainButtonType.SEND_CRIME_EVENT,
                        discordChannelId = channel.idLong,
                    )
                    mainButtonService.save(mainButton)
                }
        }
    }

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}