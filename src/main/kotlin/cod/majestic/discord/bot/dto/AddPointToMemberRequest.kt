package cod.majestic.discord.bot.dto

import cod.majestic.discord.bot.model.enums.EventCrimeType
import java.time.LocalDateTime

data class AddPointToMemberRequest(
    val asMentionUser: String,
    val typeRp: EventCrimeType,
    val points: Long,
    val actionTime: LocalDateTime,
    val whoUserActionAsMention: String,
)