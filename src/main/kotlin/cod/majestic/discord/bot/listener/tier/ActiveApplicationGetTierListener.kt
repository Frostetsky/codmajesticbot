package cod.majestic.discord.bot.listener.tier

import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.cache.DiscordRoleCache
import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.model.enums.RoleType
import cod.majestic.discord.bot.server.CodFamilyServer
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
class ActiveApplicationGetTierListener : ListenerAdapter() {

    private lateinit var discordRoleCache: DiscordRoleCache

    override fun onButtonInteraction(event: ButtonInteractionEvent) {

        val guild = event.guild ?: return
        val member = event.member ?: return
        val channel = event.channel.asTextChannel()

        when {

            event.componentId.startsWith(ComponentId.APPROVE_TIER_1.getComponentId()) -> {
                if (!hasLeaderPermissions(member)) {
                    sendPermissionError(event)
                    return
                }

                val targetId = ComponentId.APPROVE_TIER_1.getIdentifierParam<Long>(event.componentId)
                val targetMember = guild.retrieveMemberById(targetId).complete()

                val tier1Add = discordRoleCache.getTier1()
                val tier2Remove = discordRoleCache.getTier2()

                guild.removeRoleFromMember(targetMember, tier2Remove).queue()
                guild.addRoleToMember(targetMember, tier1Add).queue()

                channel.delete().queue()
            }

            event.componentId.startsWith(ComponentId.APPROVE_TIER_2.getComponentId()) -> {
                if (!hasLeaderPermissions(member)) {
                    sendPermissionError(event)
                    return
                }

                val targetId = ComponentId.APPROVE_TIER_2.getIdentifierParam<Long>(event.componentId)
                val targetMember = guild.retrieveMemberById(targetId).complete()

                val tier2Add = discordRoleCache.getTier2()
                val tier3Remove = discordRoleCache.getTier3()

                guild.removeRoleFromMember(targetMember, tier3Remove).queue()
                guild.addRoleToMember(targetMember, tier2Add).queue()

                channel.delete().queue()
            }

            event.componentId.startsWith(ComponentId.APPROVE_TIER_3.getComponentId()) -> {
                if (!hasLeaderPermissions(member)) {
                    sendPermissionError(event)
                    return
                }

                val targetId = ComponentId.APPROVE_TIER_3.getIdentifierParam<Long>(event.componentId)
                val targetMember = guild.retrieveMemberById(targetId).complete()

                val tier3Role = discordRoleCache.getTier3()

                guild.addRoleToMember(targetMember, tier3Role).queue()

                channel.delete().queue()
            }
        }
    }

    private fun hasLeaderPermissions(member: Member): Boolean {
        return member.roles.any {
            it.name == RoleType.LEADER.nameInDiscord || it.name == RoleType.DEPUTY_LEADER.nameInDiscord
        }
    }

    private fun sendPermissionError(buttonInteractionEvent: ButtonInteractionEvent) {
        buttonInteractionEvent.reply("Недостаточно прав.")
            .setEphemeral(true)
            .queue()
    }

    @Autowired
    @Lazy
    fun setDiscordChannelCache(discordRoleCache: DiscordRoleCache) {
        this.discordRoleCache = discordRoleCache
    }
}