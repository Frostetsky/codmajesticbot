package cod.majestic.discord.bot.service.impl

import cod.majestic.discord.bot.model.InactiveMember
import cod.majestic.discord.bot.repository.InactiveMemberRepository
import cod.majestic.discord.bot.service.InactiveMemberService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InactiveMemberServiceImpl(
    private val inactiveMemberRepository: InactiveMemberRepository
) : InactiveMemberService {

    override fun save(inactiveMember: InactiveMember): InactiveMember =
        inactiveMemberRepository.save(inactiveMember)

    override fun findByDiscordUserIds(userIds: List<Long>): List<InactiveMember> =
        inactiveMemberRepository.findAllByDiscordUserIdIn(userIds)

    @Transactional
    override fun deleteByDiscordUserId(discordUserId: Long) =
        inactiveMemberRepository.deleteByDiscordUserId(discordUserId)
}