package cod.majestic.discord.bot.service.button.impl

import cod.majestic.discord.bot.builder.EventCrimeMenuBuilder
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
            .find { it.name.contains("└・активность-и-баллы⚔\uFE0F") } ?: return

        if (!mainButtonService.existsByButton(MainButtonType.SEND_CRIME_EVENT)) {
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

    fun isBot(messageUserId: String, botUserId: String) = messageUserId == botUserId

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}