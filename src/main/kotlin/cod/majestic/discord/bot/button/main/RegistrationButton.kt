package cod.majestic.discord.bot.button.main

import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.model.MainButton
import cod.majestic.discord.bot.model.enums.MainButtonType
import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.server.CodFamilyServer
import cod.majestic.discord.bot.service.MainButtonService
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.util.*

@Component
class RegistrationButton(
    private val discordChannelCache: DiscordChannelCache,
    private val mainButtonService: MainButtonService
) : InitializingBean {

    private fun createAndSendButton() {
        val channel = discordChannelCache.getBy(MainChannelType.REGISTRATION)

        if (!mainButtonService.existsByButton(MainButtonType.REGISTRATION)) {

            val button = Button.primary(
                MainButtonType.REGISTRATION.id,
                MainButtonType.REGISTRATION.label
            )

            val action = channel.sendMessageEmbeds(MainButtonType.REGISTRATION.message)
                .setActionRow(button)
                .complete()

            val mainButton = MainButton(
                id = UUID.randomUUID(),
                discordMessageId = action.idLong,
                MainButtonType.REGISTRATION,
                action.channel.idLong
            )

            mainButtonService.save(mainButton)
        }
    }

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}