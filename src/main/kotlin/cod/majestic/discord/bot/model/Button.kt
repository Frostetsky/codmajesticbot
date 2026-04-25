package cod.majestic.discord.bot.model

import jakarta.persistence.Entity
import java.util.UUID
import org.springframework.data.annotation.Id

@Entity
data class Button(

    @Id
    private val id: UUID,

    /**
     * Идентификатор сообщения на котором висит кнопка на Discord сервере
     */
    private val discordMessageId: Long,

    /**
     * Название кнопки
     */
    private val nameButton: String,


    /**
     * Идентификатор канала в котором расположено сообщение и кнопка
     */
    private val discordChannelId: Long
)