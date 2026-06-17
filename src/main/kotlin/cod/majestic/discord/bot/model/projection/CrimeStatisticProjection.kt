package cod.majestic.discord.bot.model.projection

import java.time.LocalDateTime


interface CrimeStatisticProjection {
    fun getDiscordUserId(): Long
    fun getCountRp(): Long
    fun getLastActionTime(): LocalDateTime
    fun getEventCrimeServer(): String
    fun getPoints(): Long
}