package cod.majestic.discord.bot.model

import jakarta.persistence.Entity
import org.springframework.data.annotation.Id
import java.util.*

@Entity
data class MainChannel(

    @Id
    private val id: UUID,

    /**
     * Название кнопки
     */
    private val nameChannel: String,

    /**
     * Идентификатор канала на дискорд сервере
     */
    private val discordChannelId: Long
)