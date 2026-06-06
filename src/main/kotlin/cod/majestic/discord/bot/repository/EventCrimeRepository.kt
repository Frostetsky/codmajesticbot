package cod.majestic.discord.bot.repository

import cod.majestic.discord.bot.model.EventCrime
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface EventCrimeRepository : JpaRepository<EventCrime, UUID>