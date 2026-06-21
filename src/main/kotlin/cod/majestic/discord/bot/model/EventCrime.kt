package cod.majestic.discord.bot.model

import cod.majestic.discord.bot.model.enums.EventCrimeServer
import cod.majestic.discord.bot.model.enums.EventCrimeType
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "event_crime", schema = "cod_majestic_rp_bot")
data class EventCrime(

    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "event_crime_type")
    @Enumerated(EnumType.STRING)
    var type: EventCrimeType,

    @Column(name = "action_time")
    var actionTime: LocalDateTime,

    @Column(name = "event_crime_server")
    @Enumerated(EnumType.STRING)
    var server: EventCrimeServer,

    /**
     * Идентификатор сообщения в канале активности членов семьи
     */
    @Column(name = "discord_message_id")
    var discordMessageId: Long?,

    @ManyToOne
    @JoinColumn(name = "family_member_id", referencedColumnName = "id")
    var familyMember: FamilyMember
)