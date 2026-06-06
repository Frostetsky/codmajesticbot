package cod.majestic.discord.bot.button.main

import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.model.MainButton
import cod.majestic.discord.bot.model.enums.MainButtonType
import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.server.CodFamilyServer
import cod.majestic.discord.bot.service.MainButtonService
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreateApplicationGetTierButton(
    private val mainButtonService: MainButtonService,
    private val discordChannelCache: DiscordChannelCache
) : InitializingBean {

    private fun createAndSendButton() {
        val channel = discordChannelCache.getBy(MainChannelType.GET_TIER)

        if (!mainButtonService.existsByButton(MainButtonType.GET_TIER)) {

            val button = Button.primary(
                MainButtonType.GET_TIER.id,
                MainButtonType.GET_TIER.label
            )

            val action = channel.sendMessageEmbeds(MainButtonType.GET_TIER.message)
                .setActionRow(button)
                .complete()

            val mainButton = MainButton(
                id = UUID.randomUUID(),
                discordMessageId = action.idLong,
                MainButtonType.GET_TIER,
                action.channel.idLong
            )

            mainButtonService.save(mainButton)
        }
    }

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}