package cod.majestic.discord.bot.listener.application.`in`.family

import cod.majestic.discord.bot.cache.DiscordRoleCache
import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.model.enums.RoleType
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class ActiveApplicationInFamilyListener(
    private val discordRoleCache: DiscordRoleCache
) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {

        val guild = event.guild ?: return
        val member = event.member ?: return
        val channel = event.channel.asTextChannel()

        when {

            event.componentId.startsWith(ComponentId.FAMILY_APPLICATION_REJECT.getComponentId()) -> {
                if (!hasLeaderPermissions(member)) {
                    sendPermissionError(event)
                    return
                }

                val targetId = ComponentId.FAMILY_APPLICATION_REJECT.getIdentifierParam<Long>(event.componentId)
                val targetMember = guild.retrieveMemberById(targetId).complete()
                val waitingCallRole = discordRoleCache.getWaitingCall()

                guild.removeRoleFromMember(targetMember, waitingCallRole).queue()

                channel.delete().queue()
            }

            event.componentId.startsWith(ComponentId.FAMILY_APPLICATION_APPROVED.getComponentId()) -> {
                if (!hasLeaderPermissions(member)) {
                    sendPermissionError(event)
                    return
                }

                val targetId = ComponentId.FAMILY_APPLICATION_APPROVED.getIdentifierParam<Long>(event.componentId)
                val targetMember = guild.retrieveMemberById(targetId).complete()

                val testRole = discordRoleCache.getTest()
                val waitingCallRole = discordRoleCache.getWaitingCall()

                guild.addRoleToMember(targetMember, testRole).queue()
                guild.removeRoleFromMember(targetMember, waitingCallRole).queue()

                channel.delete().queue()
            }

            event.componentId.startsWith(ComponentId.GIVE_WAITING_CALL.getComponentId()) -> {
                if (!hasLeaderPermissions(member)) {
                    sendPermissionError(event)
                    return
                }

                val targetId = ComponentId.GIVE_WAITING_CALL.getIdentifierParam<Long>(event.componentId)
                val targetMember = guild.retrieveMemberById(targetId).complete()

                val waitingCallRole = discordRoleCache.getWaitingCall()
                val waitingChannel = guild.channels.find { it.name == "├・ожидание-обзвона◀\uFE0F" }

                guild.addRoleToMember(targetMember, waitingCallRole).queue()

                targetMember.user.openPrivateChannel().queue { privateChannel ->
                    privateChannel.sendMessage(
                        """
                        ✅ Твоя заявка рассмотрена и одобрена!
            
                        Теперь ты находишься в списке на ожидание обзвона.
                        Пожалуйста, перейди в канал ${waitingChannel?.asMention} и ожидай дальнейших инструкций.
                        """.trimIndent()
                    ).queue {
                        event.reply("✅ Пользователь ${targetMember.asMention} уведомлён в личные сообщения.")
                            .setEphemeral(true)
                            .queue()
                    }
                }
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

    private fun sendPermissionError(buttonInteractionEvent: ButtonInteractionEvent) {
        buttonInteractionEvent.reply("Недостаточно прав.")
            .setEphemeral(true)
            .queue()
    }
}