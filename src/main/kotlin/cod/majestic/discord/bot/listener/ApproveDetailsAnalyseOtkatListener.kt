package cod.majestic.discord.bot.listener

import cod.majestic.discord.bot.model.enums.RoleType
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class ApproveDetailsAnalyseOtkatListener : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.componentId.startsWith("approve_details_analyse_otkat_")) {
            val member = event.member ?: return
            val channel = event.channel.asTextChannel()

            if (!hasLeaderPermissions(member)) {
                event.reply("Недостаточно прав.")
                    .setEphemeral(true)
                    .queue()
                return
            }

            channel.delete().queue()
        }
    }

    private fun hasLeaderPermissions(member: Member): Boolean {
        return member.roles.any {
            it.name == RoleType.LEADER.nameInDiscord || it.name == RoleType.DEPUTY_LEADER.nameInDiscord
        }
    }
}