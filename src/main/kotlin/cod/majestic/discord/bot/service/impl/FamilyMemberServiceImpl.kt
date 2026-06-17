package cod.majestic.discord.bot.service.impl

import cod.majestic.discord.bot.model.FamilyMember
import cod.majestic.discord.bot.repository.FamilyMemberRepository
import cod.majestic.discord.bot.service.FamilyMemberService
import org.springframework.stereotype.Service

@Service
class FamilyMemberServiceImpl(
    private val familyMemberRepository: FamilyMemberRepository
) : FamilyMemberService {

    override fun getPointsByUserId(discordUserId: Long): Long = getByUserId(discordUserId).points

    override fun getByUserId(discordUserId: Long): FamilyMember =
        familyMemberRepository.findByDiscordUserId(discordUserId)

    override fun existsByDiscordUserId(discordUserId: Long): Boolean =
        familyMemberRepository.existsByDiscordUserId(discordUserId)

    override fun save(familyMember: FamilyMember): FamilyMember = familyMemberRepository.save(familyMember)
}