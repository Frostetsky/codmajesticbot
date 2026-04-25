package cod.majestic.discord.bot.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToOne
import org.springframework.data.annotation.Id
import java.util.*

@Entity
data class User(

    @Id
    private val id: UUID,

    private val profileName: String,

    private val username: String,

    private val discordUserId: Long,

    private val isBot: Boolean,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    private val point: Point
)