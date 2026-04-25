package cod.majestic.discord.bot.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToOne
import java.util.UUID
import org.springframework.data.annotation.Id

@Entity
data class Point(

    @Id
    private val id: UUID,

    private val pointCount: Long,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    private val user: User
)