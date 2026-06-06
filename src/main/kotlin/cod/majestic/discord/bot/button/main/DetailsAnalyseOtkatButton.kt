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
class DetailsAnalyseOtkatButton(
    private val discordChannelCache: DiscordChannelCache,
    private val mainButtonService: MainButtonService
) : InitializingBean {

    private fun createAndSendButton() {
        val channel = discordChannelCache.getBy(MainChannelType.DETAILS_ANALYSE_OTKAT)

        if (!mainButtonService.existsByButton(MainButtonType.DETAILS_OTKAT)) {

            val button = Button.primary(
                MainButtonType.DETAILS_OTKAT.id,
                MainButtonType.DETAILS_OTKAT.label
            )

            val action = channel.sendMessageEmbeds(MainButtonType.DETAILS_OTKAT.message)
                .setActionRow(button)
                .complete()

            val mainButton = MainButton(
                id = UUID.randomUUID(),
                discordMessageId = action.idLong,
                MainButtonType.DETAILS_OTKAT,
                action.channel.idLong
            )

            mainButtonService.save(mainButton)
        }
    }

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}