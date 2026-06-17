package cod.majestic.discord.bot.service

import cod.majestic.discord.bot.model.InactiveMember

interface InactiveMemberService {

    fun save(inactiveMember: InactiveMember): InactiveMember

    fun findByDiscordUserIds(userIds: List<Long>): List<InactiveMember>

    fun deleteByDiscordUserId(discordUserId: Long)
}