package cod.majestic.discord.bot.service.impl

import cod.majestic.discord.bot.model.PersonalBranch
import cod.majestic.discord.bot.repository.PersonalBranchRepository
import cod.majestic.discord.bot.service.PersonalBranchService
import org.springframework.stereotype.Service

@Service
class PersonalBranchServiceImpl(
    private val personalBranchRepository: PersonalBranchRepository
) : PersonalBranchService {

    override fun save(personalBranch: PersonalBranch): PersonalBranch =
        personalBranchRepository.save(personalBranch)

    override fun findByDiscordUserId(discordUserId: Long): PersonalBranch =
        personalBranchRepository.findByDiscordUserId(discordUserId)

    override fun existsByDiscordUserId(discordUserId: Long): Boolean =
        personalBranchRepository.existsByDiscordUserId(discordUserId)

}