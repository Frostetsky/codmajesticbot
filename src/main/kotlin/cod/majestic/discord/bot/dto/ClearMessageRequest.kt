package cod.majestic.discord.bot.dto

data class ClearMessageRequest(
    val channelId: Long,
    val count: Int
)
