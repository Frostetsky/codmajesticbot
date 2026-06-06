package cod.majestic.discord.bot.model

import jakarta.persistence.Entity
import org.springframework.data.annotation.Id
import java.util.*

@Entity
data class FamilyMember(

    @Id
    var id: UUID,

    var discordProfileName: String,

    var discordUsername: String,

    var discordUserId: Long,

    var points: Long
)