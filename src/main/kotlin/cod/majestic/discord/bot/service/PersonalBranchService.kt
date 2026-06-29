package cod.majestic.discord.bot.service

import cod.majestic.discord.bot.model.PersonalBranch
import java.util.Optional

interface PersonalBranchService {

    fun save(personalBranch: PersonalBranch): PersonalBranch

    fun findByDiscordUserId(discordUserId: Long): PersonalBranch

    fun existsByDiscordUserId(discordUserId: Long): Boolean

    fun findByBranchName(name: String): PersonalBranch
}