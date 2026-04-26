package cod.majestic.discord.bot.server

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import org.springframework.stereotype.Component

@Component
class CodFamilyServer(
    private val jda: JDA
) {

    fun getMyGuild(): Guild = jda.guilds.first()
}