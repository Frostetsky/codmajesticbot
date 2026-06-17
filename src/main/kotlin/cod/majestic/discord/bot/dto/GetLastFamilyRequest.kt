package cod.majestic.discord.bot.dto

data class GetLastFamilyRequest(
    val limit: Int,
    val days: Long
)