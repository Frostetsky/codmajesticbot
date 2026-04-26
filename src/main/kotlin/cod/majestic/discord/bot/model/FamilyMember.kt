package cod.majestic.discord.bot.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import org.springframework.data.annotation.Id
import java.util.*
import kotlin.collections.List

@Entity
data class FamilyMember(

    @Id
    var id: UUID,

    var discordProfileName: String,

    var discordUsername: String,

    var discordUserId: Long,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var points: List<Point>,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var eventsCrime: List<EventCrime>
)