package cod.majestic.discord.bot.config

import cod.majestic.discord.bot.config.properties.DiscordProperties
import cod.majestic.discord.bot.listener.*
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
            activeApplicationGetRangListener: ActiveApplicationGetRangListener,
            activeApplicationInFamilyListener: ActiveApplicationInFamilyListener,
            createApplicationGetRangListener: CreateApplicationGetRangListener,
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
                createApplicationGetRangListener,
                activeApplicationGetRangListener,
                createApplicationInFamilyListener
            )
            .build()
            .awaitReady()
    }
}