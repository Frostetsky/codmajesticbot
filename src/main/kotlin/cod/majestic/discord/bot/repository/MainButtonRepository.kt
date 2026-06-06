package cod.majestic.discord.bot.repository

import cod.majestic.discord.bot.model.MainButton
import cod.majestic.discord.bot.model.enums.MainButtonType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MainButtonRepository : JpaRepository<MainButton, UUID> {

    fun existsByNameButton(mainButtonType: MainButtonType): Boolean

    fun existsByNameButtonIn(mainButtonTypes: Collection<MainButtonType>): Boolean
}