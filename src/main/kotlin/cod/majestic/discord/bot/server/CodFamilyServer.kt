package cod.majestic.discord.bot.server

import cod.majestic.discord.bot.exception.NoSuchUserException
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel
import org.springframework.stereotype.Component

@Component
class CodFamilyServer(
    private val jda: JDA
) {

    fun getMyGuild(): Guild = jda.guilds.first()

    fun getTextChannels(): List<TextChannel> = getMyGuild().textChannels

    fun getVoiceChannel(): List<VoiceChannel> = getMyGuild().voiceChannels

    fun getUserById(userId: Long) = jda.retrieveUserById(userId).complete()
        ?: throw NoSuchUserException("Пользователь с $userId не найден на дискорд сервере.")
}