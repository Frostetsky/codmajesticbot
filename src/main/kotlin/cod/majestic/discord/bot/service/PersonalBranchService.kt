package cod.majestic.discord.bot.service

import cod.majestic.discord.bot.model.PersonalBranch

interface PersonalBranchService {

    fun save(personalBranch: PersonalBranch): PersonalBranch

    fun findByDiscordUserId(discordUserId: Long): PersonalBranch

    fun existsByDiscordUserId(discordUserId: Long): Boolean
}