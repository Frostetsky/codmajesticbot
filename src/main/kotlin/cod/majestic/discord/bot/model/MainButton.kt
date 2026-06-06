package cod.majestic.discord.bot.model

import cod.majestic.discord.bot.model.enums.MainButtonType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
class MainButton(

    @Id
    var id: UUID,

    var discordMessageId: Long,

    var nameButton: MainButtonType,

    var discordChannelId: Long
)