package cod.majestic.discord.bot.model

import cod.majestic.discord.bot.model.enums.RoleType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.springframework.data.annotation.Id
import java.util.UUID

@Entity
data class Role(

    @field:Id
    private val id: UUID,

    @field:Enumerated(EnumType.STRING)
    private val roleName: RoleType,

    private val discordRoleId: Long
)
