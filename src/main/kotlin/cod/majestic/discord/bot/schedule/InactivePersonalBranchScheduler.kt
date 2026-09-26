package cod.majestic.discord.bot.schedule

import cod.majestic.discord.bot.server.CodFamilyServer
import org.springframework.stereotype.Component

@Component
class InactivePersonalBranchScheduler(
    private val codFamilyServer: CodFamilyServer
) {

    //@Scheduled
    fun checkInactive() {
        val t = 0
    }
}