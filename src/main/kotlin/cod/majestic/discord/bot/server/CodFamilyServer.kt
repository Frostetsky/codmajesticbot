package cod.majestic.discord.bot.server

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.components.ActionRow
import org.springframework.stereotype.Component

@Component
class CodFamilyServer(
    private val jda: JDA
) {

    fun getMyGuild(): Guild = jda.guilds.first()

    fun getTextChannels(): List<TextChannel> = getMyGuild().textChannels

    fun getVoiceChannel(): List<VoiceChannel> = getMyGuild().voiceChannels
}