package cod.majestic.discord.bot.listener

import cod.majestic.discord.bot.model.enums.RoleType
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class ActiveApplicationGetRangListener : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {

        val guild = event.guild ?: return
        val member = event.member ?: return
        val channel = event.channel.asTextChannel()

        when {

            event.componentId.startsWith("approve_main_get_rang_") -> {
                if (!hasLeaderPermissions(member)) {
                    event.reply("Недостаточно прав.")
                        .setEphemeral(true)
                        .queue()
                    return
                }

                val targetId = event.componentId.removePrefix("approve_main_get_rang_")
                val targetMember = guild.retrieveMemberById(targetId).complete()

                val mainRole = guild.roles.find { it.name == RoleType.MAIN.nameInDiscord }
                val codRole = guild.roles.find { it.name == RoleType.COD.nameInDiscord }

                mainRole?.let {
                    guild.removeRoleFromMember(targetMember, codRole!!).queue()
                    guild.addRoleToMember(targetMember, it).queue()
                }

                channel.delete().queue()
            }

            event.componentId.startsWith("approve_main_plus_get_rang_") -> {
                if (!hasLeaderPermissions(member)) {
                    event.reply("Недостаточно прав.")
                        .setEphemeral(true)
                        .queue()
                    return
                }

                val targetId = event.componentId.removePrefix("approve_main_plus_get_rang_")
                val targetMember = guild.retrieveMemberById(targetId).complete()

                val mainPlusRole = guild.roles.find { it.name == RoleType.MAIN_PLUS.nameInDiscord }
                val mainRole = guild.roles.find { it.name == RoleType.MAIN.nameInDiscord }

                mainPlusRole?.let {
                    guild.removeRoleFromMember(targetMember, mainRole!!).queue()
                    guild.addRoleToMember(targetMember, it).queue()
                }

                channel.delete().queue()
            }

            event.componentId.startsWith("approve_cod_get_rang_") -> {
                if (!hasLeaderPermissions(member)) {
                    event.reply("Недостаточно прав.")
                        .setEphemeral(true)
                        .queue()
                    return
                }

                val targetId = event.componentId.removePrefix("approve_cod_get_rang_")
                val targetMember = guild.retrieveMemberById(targetId).complete()

                val codRole = guild.roles.find { it.name == RoleType.COD.nameInDiscord }
                val rpRole = guild.roles.find { it.name == RoleType.RP.nameInDiscord }

                codRole?.let {
                    guild.removeRoleFromMember(targetMember, rpRole!!).queue()
                    guild.addRoleToMember(targetMember, it).queue()
                }

                channel.delete().queue()
            }
        }
    }

    private fun hasLeaderPermissions(member: Member): Boolean {
        return member.roles.any {
            it.name == RoleType.LEADER.nameInDiscord
                    || it.name == RoleType.DEPUTY_LEADER.nameInDiscord
                    || it.name == RoleType.HIGH.nameInDiscord
        }
    }
}