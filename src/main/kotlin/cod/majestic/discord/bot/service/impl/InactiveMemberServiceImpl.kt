package cod.majestic.discord.bot.service.impl

import cod.majestic.discord.bot.model.InactiveMember
import cod.majestic.discord.bot.service.InactiveMemberService
import org.springframework.stereotype.Service

@Service
class InactiveMemberServiceImpl : InactiveMemberService {

    override fun save(inactiveMember: InactiveMember): InactiveMember {
        TODO("Not yet implemented")
    }

    override fun findByDiscordUserIds(userIds: List<Long>): List<InactiveMember> {
        TODO("Not yet implemented")
    }

    override fun deleteByDiscordUserId(discordUserId: Long) {
        TODO("Not yet implemented")
    }
}