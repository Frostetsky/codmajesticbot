package cod.majestic.discord.bot.monitoring

import net.dv8tion.jda.api.JDA
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MonitoringActivityPersonalBranchScheduler(
    private val jda: JDA
) {


    //@Scheduled(initialDelay = 10_000, fixedRate = 60_000)
    fun monitoring() {
        val guild = jda.guilds.first()
        guild.categories.find { it.name == "" }?.textChannels
    }
}