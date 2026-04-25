package cod.majestic.discord.bot.repository

import cod.majestic.discord.bot.model.Button
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ButtonRepository : JpaRepository<Button, UUID> {
}