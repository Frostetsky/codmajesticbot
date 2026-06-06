package cod.majestic.discord.bot.model

import cod.majestic.discord.bot.model.enums.EventCrimeServer
import cod.majestic.discord.bot.model.enums.EventCrimeType
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.springframework.data.annotation.Id
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class EventCrime(

    @Id
    var id: UUID,

    var type: EventCrimeType,

    var actionTime: LocalDateTime,

    var server: EventCrimeServer,

    /**
     * Идентификатор сообщения в канале активности членов семьи
     */
    var discordMessageId: Long,

    @ManyToOne
    @JoinColumn(columnDefinition = "family_member_id")
    var familyMember: FamilyMember
)