package cod.majestic.discord.bot.service

interface PointService {

    fun getPointsByUserId(userId: Long): Long
}