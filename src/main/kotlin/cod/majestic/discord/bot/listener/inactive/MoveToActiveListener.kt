package cod.majestic.discord.bot.listener.inactive

import cod.majestic.discord.bot.cache.DiscordRoleCache
import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.server.CodFamilyServer
import cod.majestic.discord.bot.service.InactiveMemberService
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class MoveToActiveListener(
    private val codFamilyServer: CodFamilyServer,
    private val discordRoleCache: DiscordRoleCache,
    private val inactiveMemberService: InactiveMemberService
) : ListenerAdapter() {


    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (!event.componentId.startsWith(ComponentId.MOVE_TO_ACTIVE.getComponentId())) return

        event.deferReply(true).complete()

        val userId = event.user.idLong
        val targetMember = codFamilyServer.getUserById(userId)
        val inactiveRole = discordRoleCache.getInactive()

        codFamilyServer.getMyGuild().removeRoleFromMember(targetMember, inactiveRole).complete()
        inactiveMemberService.deleteByDiscordUserId(userId)

        event.hook.setEphemeral(true)
            .sendMessage("Вы вернулись из неактива.")
            .complete()
    }
}