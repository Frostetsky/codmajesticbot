package cod.majestic.discord.bot.repository

import cod.majestic.discord.bot.model.InactiveMember
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface InactiveMemberRepository : CrudRepository<InactiveMember, UUID> {

    fun deleteByDiscordUserId(id: Long)

    fun findAllByDiscordUserIdIn(discordUserIds: List<Long>): List<InactiveMember>
}