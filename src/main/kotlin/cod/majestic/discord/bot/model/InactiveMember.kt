package cod.majestic.discord.bot.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "inactive_member", schema = "cod_majestic_rp_bot")
data class InactiveMember(

    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "discord_user_id")
    var discordUserId: Long,

    @Column(name = "description")
    var description: String,

    @Column(name = "action_time")
    var actionTime: LocalDateTime
)