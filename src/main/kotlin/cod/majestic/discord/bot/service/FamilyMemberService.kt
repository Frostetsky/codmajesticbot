package cod.majestic.discord.bot.service

import cod.majestic.discord.bot.model.FamilyMember

interface FamilyMemberService {

    fun getPointsByUserId(discordUserId: Long): Long

    fun getByUserId(discordUserId: Long): FamilyMember

    fun existsByDiscordUserId(discordUserId: Long): Boolean

    fun save(familyMember: FamilyMember): FamilyMember
}