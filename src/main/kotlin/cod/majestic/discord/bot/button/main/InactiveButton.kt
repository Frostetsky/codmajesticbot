package cod.majestic.discord.bot.button.main

import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.model.MainButton
import cod.majestic.discord.bot.model.enums.MainButtonType
import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.service.MainButtonService
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.awt.Color
import java.util.*

@Component
class InactiveButton(
    private val discordChannelCache: DiscordChannelCache,
    private val mainButtonService: MainButtonService
) : InitializingBean {

    private fun createAndSendButton() {
        val channel = discordChannelCache.getBy(MainChannelType.INACTIVE)

        if (!mainButtonService.existsByButtons(setOf(MainButtonType.MOVE_TO_INACTIVE, MainButtonType.MOVE_TO_ACTIVE))) {

            val moveToInactiveButton = Button.danger(
                MainButtonType.MOVE_TO_INACTIVE.id,
                MainButtonType.MOVE_TO_INACTIVE.label
            )

            val moveToActiveButton = Button.success(
                MainButtonType.MOVE_TO_ACTIVE.id,
                MainButtonType.MOVE_TO_ACTIVE.label
            )

            val embed = EmbedBuilder()
                .setTitle("Активность и неактивность")
                .setDescription(
                    """
                    **В данном канале вы можете:**
                    
                    1. ${MainButtonType.MOVE_TO_INACTIVE.label}
                    2. ${MainButtonType.MOVE_TO_ACTIVE.label}
                    
                    📌 Уйти в неактив можно максимум на 24 часа.
                    📌 Если вы уходите в неактив более чем на 24 часа, вы должны указать причину.
                    📌 Запрещено брать неактив более чем на 2 недели.
                    📌 Если вам нужен более долгий период отсутствия, то вы кикайтесь из семьи и можете восстановиться в неё позже.
                    
                    """.trimIndent()
                )
                .setColor(Color.RED)
                .build()

            val message = channel.sendMessageEmbeds(embed)
                .setComponents(
                    ActionRow.of(moveToInactiveButton),
                    ActionRow.of(moveToActiveButton),
                )
                .complete()

            val toInactiveButtonEntity = MainButton(
                id = UUID.randomUUID(),
                discordMessageId = message.idLong,
                MainButtonType.MOVE_TO_INACTIVE,
                message.channel.idLong
            )

            val toActiveButtonEntity = MainButton(
                id = UUID.randomUUID(),
                discordMessageId = message.idLong,
                MainButtonType.MOVE_TO_ACTIVE,
                message.channel.idLong
            )

            mainButtonService.saveAll(setOf(toInactiveButtonEntity, toActiveButtonEntity))
        }
    }

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}