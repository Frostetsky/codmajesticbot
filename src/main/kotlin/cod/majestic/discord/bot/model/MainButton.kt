package cod.majestic.discord.bot.model

import cod.majestic.discord.bot.model.enums.MainButtonType
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "main_button", schema = "cod_majestic_rp_bot")
data class MainButton(

    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "discord_message_id")
    var discordMessageId: Long,

    @Column(name = "name_button")
    @Enumerated(EnumType.STRING)
    var nameButton: MainButtonType,

    @Column(name = "discord_channel_id")
    var discordChannelId: Long
)