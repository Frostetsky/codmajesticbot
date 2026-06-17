package cod.majestic.discord.bot.listener.tier

import cod.majestic.discord.bot.cache.DiscordRoleCache
import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.model.enums.RoleType
import cod.majestic.discord.bot.server.CodFamilyServer
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.stereotype.Component
import cod.majestic.discord.bot.model.enums.RoleType as RoleCod

@Component
class CreateApplicationGetTierListener(
    private val codFamilyServer: CodFamilyServer,
    private val discordRoleCache: DiscordRoleCache
) : ListenerAdapter() {

    private val tierRoles = setOf(RoleCod.TIER_1.nameInDiscord, RoleCod.TIER_2.nameInDiscord, RoleType.TIER_3.nameInDiscord)

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.componentId != ComponentId.CREATE_APPLICATION_GET_TIER.getComponentId()) return

        val guild = event.guild ?: return
        val member = event.member ?: return

        val categoryGetRang = guild.categories.firstOrNull { it.name == "\uD83D\uDD2B・Tier" }!!
        val existingChannel = guild.textChannels
            .firstOrNull { it.name == "\uD83D\uDEB9・заявка-на-получение-тира-${member.user.id}" }

        if (existingChannel != null) {
            event.reply("❌ У вас уже есть активная заявка: ${existingChannel.asMention}")
                .setEphemeral(true)
                .queue()
            return
        }

        val tier1Role = member.roles.any { it.name == RoleCod.TIER_1.nameInDiscord }

        if (tier1Role) {
            event.reply("У тебя максимально возможный тир %s".format(RoleCod.TIER_1.nameInDiscord))
                .setEphemeral(true)
                .queue()
            return
        }

        val deputyRole = discordRoleCache.getDepLeader()
        val leaderRole = discordRoleCache.getLeader()
        val recruiterRole = discordRoleCache.getRecruiter()
        val mediaRole = discordRoleCache.getMedia()
        val testRole = discordRoleCache.getTest()
        val mainRole = discordRoleCache.getMain()

        val channelName = "\uD83D\uDEB9・заявка-на-получение-тира-${member.user.id}"

        categoryGetRang.createTextChannel(channelName)
            .addPermissionOverride(
                guild.publicRole,
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                recruiterRole,
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                mediaRole,
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                testRole,
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                mainRole,
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                member,
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                null
            )
            .addPermissionOverride(
                deputyRole,
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                null
            )
            .addPermissionOverride(
                leaderRole,
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                null
            )
            .queue { createdChannel ->

                val message =
                    """
                    📋 **Заявка на получение ранга**

                    Запрос от пользователя: ${member.asMention}.
                    Личная ветка пользователя: временно отсутствует
                    Просьба пересмотреть мой текущий ранг и выдать ${getMessageByPriorityRole(member.roles)}.
                    Ниже прикрепляю все необходимые доказательства.
                    
                    """.trimIndent()

                createdChannel.sendMessage(message)
                    .setActionRow(
                        Button.success(
                            ComponentId.APPROVE_TIER_1.createComponentId(member.user.id), "Выдать TIER 1"
                        ),
                        Button.danger(
                            ComponentId.APPROVE_TIER_2.createComponentId(member.user.id), "Выдать TIER 2"
                        ),
                        Button.primary(
                            ComponentId.APPROVE_TIER_3.createComponentId(member.user.id), "Выдать TIER 3"
                        )
                    )
                    .queue()

                event.reply("Заявка создана: ${createdChannel.asMention}")
                    .setEphemeral(true)
                    .queue()
            }
    }

    private fun getMessageByPriorityRole(roles: List<Role>): String {
        val noTier = roles.none { role -> tierRoles.contains(role.name) }
        val isTier2 = roles.any { it.name == RoleCod.TIER_2.nameInDiscord }
        val isTier3 = roles.any { it.name == RoleCod.TIER_3.nameInDiscord }

        if (noTier) {
            return RoleCod.TIER_3.nameInDiscord
        }

        if (isTier2) {
            return RoleCod.TIER_1.nameInDiscord
        }

        if (isTier3) {
            return RoleCod.TIER_2.nameInDiscord
        }

        throw UnsupportedOperationException("Неизвестно какой ранг запрашивает участник семьи. Произошёл конфликт ролей.")
    }
}