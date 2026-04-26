package cod.majestic.discord.bot.repository

import cod.majestic.discord.bot.model.FamilyMember
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FamilyMemberRepository : CrudRepository<FamilyMember, UUID> {
}