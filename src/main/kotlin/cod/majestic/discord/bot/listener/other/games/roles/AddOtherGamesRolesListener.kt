package cod.majestic.discord.bot.listener.other.games.roles

import cod.majestic.discord.bot.cache.DiscordRoleCache
import cod.majestic.discord.bot.model.enums.RoleType
import cod.majestic.discord.bot.server.CodFamilyServer
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class AddOtherGamesRolesListener(
    private val discordRoleCache: DiscordRoleCache,
    private val codFamilyServer: CodFamilyServer
) : ListenerAdapter() {

    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        if (event.componentId != "other_games_select_id") return

        val guild = codFamilyServer.getMyGuild()
        val member = event.member ?: throw NoSuchElementException("Member must be.")
        val roleType = RoleType.valueOf(event.values[0])

        when (roleType) {

            RoleType.CS -> {
                val csRole = discordRoleCache.getCs()
                guild.addRoleToMember(member, csRole).complete()
            }

            RoleType.DOTA -> {
                val dotaRole = discordRoleCache.getDota()
                guild.addRoleToMember(member, dotaRole).complete()
            }

            RoleType.ROBLOX -> {
                val robloxRole = discordRoleCache.getRoblox()
                guild.addRoleToMember(member, robloxRole).complete()
            }

            RoleType.TANKI -> {
                val tankiRole = discordRoleCache.getTanki()
                guild.addRoleToMember(member, tankiRole).complete()
            }

            RoleType.MINECRAFT -> {
                val minecraftRole = discordRoleCache.getMinecraft()
                guild.addRoleToMember(member, minecraftRole).complete()
            }

            RoleType.PUBG -> {
                val pubgRole = discordRoleCache.getPubg()
                guild.addRoleToMember(member, pubgRole).complete()
            }

            else -> {
                throw UnsupportedOperationException("Unsupported role type $roleType")
            }
        }

        event.reply("Вы получили роль ${roleType.nameInDiscord}").setEphemeral(true).queue()
    }
}