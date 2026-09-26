package cod.majestic.discord.bot.button.main

import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.cache.DiscordRoleCache
import cod.majestic.discord.bot.model.MainButton
import cod.majestic.discord.bot.model.enums.MainButtonType
import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.server.CodFamilyServer
import cod.majestic.discord.bot.service.MainButtonService
import com.sun.tools.javac.Main
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreateApplicationGetTierButton(
    private val mainButtonService: MainButtonService,
    private val discordChannelCache: DiscordChannelCache,
    private val discordRoleCache: DiscordRoleCache,
    private val codFamilyServer: CodFamilyServer
) : InitializingBean {

    private fun createAndSendButton() {
        val channel = discordChannelCache.getBy(MainChannelType.GET_TIER)

        if (!mainButtonService.existsByButton(MainButtonType.GET_TIER)) {

            val button = Button.primary(
                MainButtonType.GET_TIER.id,
                MainButtonType.GET_TIER.label
            )

            val embedMessage = MainButtonType.GET_TIER.message

            val tier3 = discordRoleCache.getTier3()
            val tier2 = discordRoleCache.getTier2()
            val tier1 = discordRoleCache.getTier1()
            val tierChecker = codFamilyServer.getUserById(984155244294459442)

            val modifyDescription = embedMessage.description
                ?.format(tier3.asMention, tier2.asMention, tier1.asMention, tierChecker.asMention)

            val embedModify = EmbedBuilder()
                .setTitle(MainButtonType.GET_TIER.message.title)
                .setColor(MainButtonType.GET_TIER.message.color)
                .setDescription(modifyDescription)
                .build()


            val action = channel.sendMessageEmbeds(embedModify)
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