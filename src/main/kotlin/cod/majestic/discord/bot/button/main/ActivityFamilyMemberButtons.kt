package cod.majestic.discord.bot.button.main

import cod.majestic.discord.bot.builder.EventCrimeMenuBuilder
import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.model.MainButton
import cod.majestic.discord.bot.model.enums.MainButtonType
import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.service.MainButtonService
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import java.util.*

@Service
class ActivityFamilyMemberButtons(
    private val discordChannelCache: DiscordChannelCache,
    private val mainButtonService: MainButtonService
) : InitializingBean {

    private fun createAndSendButton() {
        val channel = discordChannelCache.getBy(MainChannelType.ACTIVITY_FAMILY_MEMBER_MAIN)

        if (!mainButtonService.existsByButtons(setOf(MainButtonType.GET_COUNT_MY_POINTS, MainButtonType.SEND_CRIME_EVENT))) {

            val getMyPointsButton = Button.primary(
                MainButtonType.GET_COUNT_MY_POINTS.id,
                MainButtonType.GET_COUNT_MY_POINTS.label
            )

            val selectMenuSendCrimeEventButton = EventCrimeMenuBuilder.get()
            channel.sendMessage(
                """
                    Нажмите ниже чтобы отправить информацию об участии в семейном событии.
                    Укажите сервер, приложите ссылку.
                    """.trimIndent()
            )

            val action = channel.sendMessageEmbeds(MainButtonType.GET_COUNT_MY_POINTS.message)
                .setActionRow(getMyPointsButton, selectMenuSendCrimeEventButton)
                .complete()

            val getMyPointsButtonSaved = MainButton(
                id = UUID.randomUUID(),
                discordMessageId = action.idLong,
                MainButtonType.GET_COUNT_MY_POINTS,
                action.channel.idLong
            )

            val selectMenuSendCrimeEventButtonSaved = MainButton(
                id = UUID.randomUUID(),
                discordMessageId = action.idLong,
                MainButtonType.GET_COUNT_MY_POINTS,
                action.channel.idLong
            )

            mainButtonService.saveAll(setOf(getMyPointsButtonSaved, selectMenuSendCrimeEventButtonSaved))
        }
    }

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}