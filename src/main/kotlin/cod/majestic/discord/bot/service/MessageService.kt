package cod.majestic.discord.bot.service

import net.dv8tion.jda.api.entities.Message

interface MessageService {

    fun getMessageFromActivityMemberFamilyChannel(id: Long): Message
}