package cod.majestic.discord.bot.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "family_member", schema = "cod_majestic_rp_bot")
data class FamilyMember(

    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "discord_profile_name")
    var discordProfileName: String,

    @Column(name = "discord_username")
    var discordUsername: String,

    @Column(name = "discord_user_id")
    var discordUserId: Long,

    @Column(name = "points")
    var points: Long
)