package cod.majestic.discord.bot.model

import cod.majestic.discord.bot.model.enums.MainButtonType
import jakarta.persistence.Entity
import java.util.UUID
import org.springframework.data.annotation.Id
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

@Entity
data class MainButton(

    @Id
    private val id: UUID,

    /**
     * Идентификатор сообщения на котором висит кнопка на Discord сервере
     */
    private val discordMessageId: Long,

    /**
     * Название кнопки
     */
    private val nameButton: MainButtonType,


    /**
     * Идентификатор канала в котором расположено сообщение и кнопка
     */
    private val discordChannelId: Long
)