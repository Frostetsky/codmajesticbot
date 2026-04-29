package cod.majestic.discord.bot.listener

import cod.majestic.discord.bot.model.enums.RoleType
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class ActiveApplicationInFamilyListener : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {

        val guild = event.guild ?: return
        val member = event.member ?: return
        val channel = event.channel.asTextChannel()

        when {

            event.componentId.startsWith("reject_application_") -> {
                if (!hasLeaderPermissions(member)) {
                    event.reply("Недостаточно прав.")
                        .setEphemeral(true)
                        .queue()
                    return
                }

                channel.delete().queue()
            }

            event.componentId.startsWith("salary_reject_application_") -> {
                if (!hasLeaderPermissions(member)) {
                    event.reply("Недостаточно прав.")
                        .setEphemeral(true)
                        .queue()
                    return
                }

                channel.delete().queue()
            }

            event.componentId.startsWith("approve_application_") -> {
                if (!hasLeaderPermissions(member)) {
                    event.reply("Недостаточно прав.")
                        .setEphemeral(true)
                        .queue()
                    return
                }

                val targetId = event.componentId.removePrefix("approve_application_")
                val targetMember = guild.retrieveMemberById(targetId).complete()

                val rpRole = guild.roles.find { it.name == RoleType.RP.nameInDiscord }
                val tegRole = guild.roles.find { it.name == RoleType.TEG.nameInDiscord }

                rpRole?.let { guild.addRoleToMember(targetMember, it).queue() }
                tegRole?.let { guild.addRoleToMember(targetMember, it).queue() }

                channel.delete().queue()
            }

            event.componentId.startsWith("salary_approve_application_") -> {
                if (!hasLeaderPermissions(member)) {
                    event.reply("Недостаточно прав.")
                        .setEphemeral(true)
                        .queue()
                    return
                }

                val targetId = event.componentId.removePrefix("salary_approve_application_")
                val targetMember = guild.retrieveMemberById(targetId).complete()

                val mainRole = guild.roles.find { it.name == RoleType.MAIN.nameInDiscord }
                val tegRole = guild.roles.find { it.name == RoleType.TEG.nameInDiscord }

                mainRole?.let { guild.addRoleToMember(targetMember, it).queue() }
                tegRole?.let { guild.addRoleToMember(targetMember, it).queue() }

                channel.delete().queue()
            }
        }
    }

    private fun hasLeaderPermissions(member: Member): Boolean {
        return member.roles.any {
            it.name == RoleType.LEADER.nameInDiscord
                    || it.name == RoleType.DEPUTY_LEADER.nameInDiscord
                    || it.name == RoleType.HIGH.nameInDiscord
                    || it.name == RoleType.RECRUITER.nameInDiscord
        }
    }
}