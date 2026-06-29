package cod.majestic.discord.bot.repository

import cod.majestic.discord.bot.model.PersonalBranch
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface PersonalBranchRepository : CrudRepository<PersonalBranch, UUID> {

    fun findByDiscordUserId(discordUserId: Long): PersonalBranch

    fun existsByDiscordUserId(discordUserId: Long): Boolean

    fun findByBranchName(branchName: String): Optional<PersonalBranch>
}