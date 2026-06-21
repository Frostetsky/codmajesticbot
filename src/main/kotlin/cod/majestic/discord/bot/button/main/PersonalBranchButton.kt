package cod.majestic.discord.bot.button.main

import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.model.MainButton
import cod.majestic.discord.bot.model.enums.MainButtonType
import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.service.MainButtonService
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.awt.Color
import java.util.*

@Component
class PersonalBranchButton(
    private val discordChannelCache: DiscordChannelCache,
    private val mainButtonService: MainButtonService
) : InitializingBean {

    private fun createAndSendButton() {
        val channel = discordChannelCache.getBy(MainChannelType.CREATE_PERSONAL_BRANCH)

        if (!mainButtonService.existsByButton(MainButtonType.CREATE_PERSONAL_BRANCH)) {

            val button = Button.primary(
                MainButtonType.CREATE_PERSONAL_BRANCH.id,
                MainButtonType.CREATE_PERSONAL_BRANCH.label
            )

            val detailsOtkatMention = discordChannelCache.getBy(MainChannelType.DETAILS_ANALYSE_OTKAT).asMention

            val updatableMessage = EmbedBuilder()
                .setTitle(MainButtonType.CREATE_PERSONAL_BRANCH.label)
                .setDescription(MainButtonType.CREATE_PERSONAL_BRANCH.message.description?.format(detailsOtkatMention))
                .setColor(MainButtonType.CREATE_PERSONAL_BRANCH.message.color)
                .build()

            val action = channel.sendMessageEmbeds(updatableMessage)
                .setActionRow(button)
                .complete()

            val mainButton = MainButton(
                id = UUID.randomUUID(),
                discordMessageId = action.idLong,
                MainButtonType.CREATE_PERSONAL_BRANCH,
                action.channel.idLong
            )

            mainButtonService.save(mainButton)
        }
    }

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}