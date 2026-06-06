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
class CreateSalaryApplicationInFamilyButton(
    private val discordChannelCache: DiscordChannelCache,
    private val mainButtonService: MainButtonService
) : InitializingBean {

    private fun createAndSendButton() {
        val channel = discordChannelCache.getBy(MainChannelType.APPLICATION_IN_FAMILY_SALARY)

        if (!mainButtonService.existsByButton(MainButtonType.CREATE_APPLICATION_ON_SALARY_IN_FAMILY)) {

            val button = Button.primary(
                MainButtonType.CREATE_APPLICATION_ON_SALARY_IN_FAMILY.id,
                MainButtonType.CREATE_APPLICATION_ON_SALARY_IN_FAMILY.label
            )

            val action = channel.sendMessageEmbeds(MainButtonType.CREATE_APPLICATION_ON_SALARY_IN_FAMILY.message)
                .setActionRow(button)
                .complete()

            val mainButton = MainButton(
                id = UUID.randomUUID(),
                discordMessageId = action.idLong,
                nameButton = MainButtonType.CREATE_APPLICATION_ON_SALARY_IN_FAMILY,
                discordChannelId = action.channel.idLong
            )

            mainButtonService.save(mainButton)
        }
    }

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}