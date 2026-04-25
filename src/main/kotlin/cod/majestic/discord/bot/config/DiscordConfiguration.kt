package cod.majestic.discord.bot.config

import cod.majestic.discord.bot.listener.ActiveApplicationInFamilyListener
import cod.majestic.discord.bot.listener.CreateApplicationInFamilyListener
import cod.majestic.discord.bot.listener.SendCrimeEventListener
import cod.majestic.discord.bot.config.properties.DiscordProperties
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DiscordConfiguration(
    val discordProperties: DiscordProperties
) {

    @Bean
    fun jda(sendCrimeEventListener: SendCrimeEventListener,
            activeApplicationInFamilyListener: ActiveApplicationInFamilyListener,
            createApplicationInFamilyListener: CreateApplicationInFamilyListener
    ) : JDA {
        return JDABuilder.createDefault(discordProperties.token)
            .enableIntents(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MEMBERS
            )
            .addEventListeners(
                sendCrimeEventListener,
                activeApplicationInFamilyListener,
                createApplicationInFamilyListener
            )
            .build()
            .awaitReady()
    }
}