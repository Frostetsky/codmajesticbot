package cod.majestic.discord.bot.config

import cod.majestic.discord.bot.config.properties.DiscordProperties
import cod.majestic.discord.bot.listener.application.`in`.family.ActiveApplicationInFamilyListener
import cod.majestic.discord.bot.listener.application.`in`.family.CreateApplicationInFamilyListener
import cod.majestic.discord.bot.listener.authorization.AuthorizationListener
import cod.majestic.discord.bot.listener.crime.event.AddPointAndUpdateMessageListener
import cod.majestic.discord.bot.listener.crime.event.CancelAndUpdateMessageListener
import cod.majestic.discord.bot.listener.crime.event.SendCrimeEventListener
import cod.majestic.discord.bot.listener.details.otkat.ApproveDetailsAnalyseOtkatListener
import cod.majestic.discord.bot.listener.details.otkat.DetailsAnalyseOtkatListener
import cod.majestic.discord.bot.listener.inactive.MoveToActiveListener
import cod.majestic.discord.bot.listener.inactive.MoveToInactiveListener
import cod.majestic.discord.bot.listener.tier.ActiveApplicationGetTierListener
import cod.majestic.discord.bot.listener.tier.CreateApplicationGetTierListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class DiscordConfiguration(
    val discordProperties: DiscordProperties,
    val configurableApplicationContext: ConfigurableApplicationContext
) {

    @Bean
    fun jda() : JDA {
        return JDABuilder.createDefault(discordProperties.token)
            .enableIntents(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MEMBERS
            )
            .build()
            .awaitReady()
    }

    @EventListener(ApplicationReadyEvent::class)
    fun configureAfterCreateContext() {
        val jda = configurableApplicationContext.getBean(JDA::class.java)
        val activeApplicationGetTierListener = configurableApplicationContext.getBean(ActiveApplicationGetTierListener::class.java)
        val sendCrimeEventListener = configurableApplicationContext.getBean(SendCrimeEventListener::class.java)
        val activeApplicationInFamilyListener = configurableApplicationContext.getBean(ActiveApplicationInFamilyListener::class.java)
        val createApplicationGetTierListener = configurableApplicationContext.getBean(CreateApplicationGetTierListener::class.java)
        val createApplicationInFamilyListener = configurableApplicationContext.getBean(CreateApplicationInFamilyListener::class.java)
        val detailsAnalyseOtkatListener = configurableApplicationContext.getBean(DetailsAnalyseOtkatListener::class.java)
        val approveDetailsAnalyseOtkatListener = configurableApplicationContext.getBean(ApproveDetailsAnalyseOtkatListener::class.java)
        val authorizationListener = configurableApplicationContext.getBean(AuthorizationListener::class.java)
        val addPointAndUpdateMessageListener = configurableApplicationContext.getBean(AddPointAndUpdateMessageListener::class.java)
        val cancelAndUpdateMessageListener = configurableApplicationContext.getBean(CancelAndUpdateMessageListener::class.java)
        val moveToActiveListener = configurableApplicationContext.getBean(MoveToActiveListener::class.java)
        val moveToInactiveListener = configurableApplicationContext.getBean(MoveToInactiveListener::class.java)
        jda.addEventListener(moveToActiveListener)
        jda.addEventListener(moveToInactiveListener)
        jda.addEventListener(sendCrimeEventListener)
        jda.addEventListener(activeApplicationInFamilyListener)
        jda.addEventListener(createApplicationGetTierListener)
        jda.addEventListener(createApplicationInFamilyListener)
        jda.addEventListener(detailsAnalyseOtkatListener)
        jda.addEventListener(approveDetailsAnalyseOtkatListener)
        jda.addEventListener(authorizationListener)
        jda.addEventListener(activeApplicationGetTierListener)
        jda.addEventListener(addPointAndUpdateMessageListener)
        jda.addEventListener(cancelAndUpdateMessageListener)
        jda.awaitReady()
    }
}