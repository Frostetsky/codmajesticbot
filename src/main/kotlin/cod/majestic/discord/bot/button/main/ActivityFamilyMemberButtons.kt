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
import org.springframework.stereotype.Service
import java.awt.Color
import java.util.*

@Service
class ActivityFamilyMemberButtons(
    private val discordChannelCache: DiscordChannelCache,
    private val mainButtonService: MainButtonService
) : InitializingBean {

    private fun createAndSendButton() {
        val channel = discordChannelCache.getBy(MainChannelType.ACTIVITY_FAMILY_MEMBER_MAIN)

        if (!mainButtonService.existsByButtons(setOf(MainButtonType.GET_COUNT_MY_POINTS, MainButtonType.REMOVE_WARN))) {

            val getMyPointsButton = Button.primary(
                MainButtonType.GET_COUNT_MY_POINTS.id,
                MainButtonType.GET_COUNT_MY_POINTS.label
            )

            val removeWarn = Button.danger(
                MainButtonType.REMOVE_WARN.id,
                MainButtonType.REMOVE_WARN.label
            )

            val embed = EmbedBuilder()
                .setTitle("Активность членов семьи")
                .setDescription(
                    """
                    **В данном канале вы можете:**
                    
                    1. ${MainButtonType.GET_COUNT_MY_POINTS.label}
                    2. ${MainButtonType.REMOVE_WARN.label}
                    3. Прикрепить доказательства участия в том или ином крайм-мероприятии.
                    
                    Если на ваше сообщение об участии установлена реакция ✅, то
                    это означает что хай-состав проверил ваши доказательства и начислил баллы.
                    Если же стоит реакция ❌, то это означает что ваши доказательства не были приняты.
                    
                    """.trimIndent()
                )
                .setColor(Color.RED)
                .build()

            val message = channel.sendMessageEmbeds(embed)
                .setComponents(
                    ActionRow.of(getMyPointsButton),
                    ActionRow.of(removeWarn),
                )
                .complete()

            val getMyPointsButtonSaved = MainButton(
                id = UUID.randomUUID(),
                discordMessageId = message.idLong,
                MainButtonType.GET_COUNT_MY_POINTS,
                message.channel.idLong
            )

            val removeWarnButtonSaved = MainButton(
                id = UUID.randomUUID(),
                discordMessageId = message.idLong,
                MainButtonType.REMOVE_WARN,
                message.channel.idLong
            )

            mainButtonService.saveAll(setOf(getMyPointsButtonSaved, removeWarnButtonSaved))
        }
    }

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}