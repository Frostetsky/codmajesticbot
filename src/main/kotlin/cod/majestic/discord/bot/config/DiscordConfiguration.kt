package cod.majestic.discord.bot.config

import cod.majestic.discord.bot.config.properties.DiscordProperties
import cod.majestic.discord.bot.listener.application.`in`.family.ActiveApplicationInFamilyListener
import cod.majestic.discord.bot.listener.application.`in`.family.CreateApplicationInFamilyListener
import cod.majestic.discord.bot.listener.application.`in`.family.CreateSalaryApplicationInFamilyListener
import cod.majestic.discord.bot.listener.crime.event.SendCrimeEventListener
import cod.majestic.discord.bot.listener.details.otkat.ApproveDetailsAnalyseOtkatListener
import cod.majestic.discord.bot.listener.details.otkat.DetailsAnalyseOtkatListener
import cod.majestic.discord.bot.listener.tier.ActiveApplicationGetTierListener
import cod.majestic.discord.bot.listener.tier.CreateApplicationGetTierListener
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
            activeApplicationGetTierListener: ActiveApplicationGetTierListener,
            activeApplicationInFamilyListener: ActiveApplicationInFamilyListener,
            createApplicationGetTierListener: CreateApplicationGetTierListener,
            createApplicationInFamilyListener: CreateApplicationInFamilyListener,
            createSalaryApplicationInFamilyListener: CreateSalaryApplicationInFamilyListener,
            detailsAnalyseOtkatListener: DetailsAnalyseOtkatListener,
            approveDetailsAnalyseOtkatListener: ApproveDetailsAnalyseOtkatListener
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
                createApplicationGetTierListener,
                activeApplicationGetTierListener,
                createApplicationInFamilyListener,
                createSalaryApplicationInFamilyListener,
                detailsAnalyseOtkatListener,
                approveDetailsAnalyseOtkatListener
            )
            .build()
            .awaitReady()
    }
}