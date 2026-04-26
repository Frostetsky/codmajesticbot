package cod.majestic.discord.bot.model

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import org.springframework.data.annotation.Id
import java.util.*

@Entity
data class Point(

    @Id
    var id: UUID,

    var pointValue: Int,

    @ManyToOne
    var familyMember: FamilyMember
)