package cod.majestic.discord.bot.button.main

import cod.majestic.discord.bot.builder.MenuBuilder
import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.model.MainButton
import cod.majestic.discord.bot.model.enums.MainButtonType
import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.service.MainButtonService
import net.dv8tion.jda.api.interactions.components.ActionRow
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.util.*

@Component
class AddOtherGamesRolesMenuButton(
    private val discordChannelCache: DiscordChannelCache,
    private val mainButtonService: MainButtonService
) : InitializingBean {

    private fun createAndSendButton() {
        val channel = discordChannelCache.getBy(MainChannelType.ADD_OTHER_GAMES_ROLES)

        if (!mainButtonService.existsByButton(MainButtonType.ADD_OTHER_GAMES_ROLES)) {

            val action = channel.sendMessageEmbeds(MainButtonType.ADD_OTHER_GAMES_ROLES.message)
                .setComponents(
                    ActionRow.of(MenuBuilder.getOtherGames())
                )
                .complete()

            val mainButton = MainButton(
                id = UUID.randomUUID(),
                discordMessageId = action.idLong,
                MainButtonType.ADD_OTHER_GAMES_ROLES,
                action.channel.idLong
            )

            mainButtonService.save(mainButton)
        }
    }

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}