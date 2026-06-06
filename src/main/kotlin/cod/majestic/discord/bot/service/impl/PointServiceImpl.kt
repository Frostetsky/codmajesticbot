package cod.majestic.discord.bot.service.impl

import cod.majestic.discord.bot.service.PointService
import org.springframework.stereotype.Service

@Service
class PointServiceImpl(
    private val pointRepository: PointRepository
) : PointService {
    override fun getPointsByUserId(userId: Long): Long {
        TODO("Not yet implemented")
    }
}