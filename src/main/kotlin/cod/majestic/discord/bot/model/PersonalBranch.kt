package cod.majestic.discord.bot.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "personal_branch", schema = "cod_majestic_rp_bot")
data class PersonalBranch(

    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "discord_user_id")
    var discordUserId: Long,

    @Column(name = "branch_name")
    val branchName: String,

    @Column(name = "discord_channel_id")
    var discordChannelId: Long
)