package cod.majestic.discord.bot.config

import cod.majestic.discord.bot.config.properties.DiscordProperties
import cod.majestic.discord.bot.listener.admin.AdministrationListener
import cod.majestic.discord.bot.listener.admin.strategy.CommandHandler
import cod.majestic.discord.bot.listener.application.`in`.family.ActiveApplicationInFamilyListener
import cod.majestic.discord.bot.listener.application.`in`.family.CreateApplicationInFamilyListener
import cod.majestic.discord.bot.listener.authorization.AuthorizationListener
import cod.majestic.discord.bot.listener.details.otkat.ApproveDetailsAnalyseOtkatListener
import cod.majestic.discord.bot.listener.details.otkat.DetailsAnalyseOtkatListener
import cod.majestic.discord.bot.listener.inactive.MoveToActiveListener
import cod.majestic.discord.bot.listener.inactive.MoveToInactiveListener
import cod.majestic.discord.bot.listener.personal.branch.CreatePersonalBranchListener
import cod.majestic.discord.bot.listener.points.GetCountMyPointsListener
import cod.majestic.discord.bot.listener.tier.ActiveApplicationGetTierListener
import cod.majestic.discord.bot.listener.tier.CreateApplicationGetTierListener
import cod.majestic.discord.bot.listener.warn.RemoveWarnListener
import cod.majestic.discord.bot.model.enums.AdminCommand
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
        val activeApplicationInFamilyListener = configurableApplicationContext.getBean(ActiveApplicationInFamilyListener::class.java)
        val createApplicationGetTierListener = configurableApplicationContext.getBean(CreateApplicationGetTierListener::class.java)
        val createApplicationInFamilyListener = configurableApplicationContext.getBean(CreateApplicationInFamilyListener::class.java)
        val detailsAnalyseOtkatListener = configurableApplicationContext.getBean(DetailsAnalyseOtkatListener::class.java)
        val approveDetailsAnalyseOtkatListener = configurableApplicationContext.getBean(ApproveDetailsAnalyseOtkatListener::class.java)
        val authorizationListener = configurableApplicationContext.getBean(AuthorizationListener::class.java)
        val moveToActiveListener = configurableApplicationContext.getBean(MoveToActiveListener::class.java)
        val moveToInactiveListener = configurableApplicationContext.getBean(MoveToInactiveListener::class.java)
        val createPersonalBranchListener = configurableApplicationContext.getBean(CreatePersonalBranchListener::class.java)
        val removeWarnListener = configurableApplicationContext.getBean(RemoveWarnListener::class.java)
        val getCountMyPointsListener = configurableApplicationContext.getBean(GetCountMyPointsListener::class.java)
        val administrationListener = configurableApplicationContext.getBean(AdministrationListener::class.java)
        jda.addEventListener(administrationListener)
        jda.addEventListener(moveToActiveListener)
        jda.addEventListener(moveToInactiveListener)
        jda.addEventListener(activeApplicationInFamilyListener)
        jda.addEventListener(createApplicationGetTierListener)
        jda.addEventListener(createApplicationInFamilyListener)
        jda.addEventListener(detailsAnalyseOtkatListener)
        jda.addEventListener(approveDetailsAnalyseOtkatListener)
        jda.addEventListener(authorizationListener)
        jda.addEventListener(activeApplicationGetTierListener)
        jda.addEventListener(createPersonalBranchListener)
        jda.addEventListener(removeWarnListener)
        jda.addEventListener(getCountMyPointsListener)
        jda.awaitReady()
    }

    @Bean
    fun getAdminMap(commandHandlers: List<CommandHandler<*>>): Map<AdminCommand, CommandHandler<*>> {
        return commandHandlers.associateBy { it.command() }
    }
}