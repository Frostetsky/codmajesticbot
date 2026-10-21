package cod.majestic.discord.bot.listener.tier

import cod.majestic.discord.bot.cache.DiscordRoleCache
import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.model.enums.RoleType
import cod.majestic.discord.bot.server.CodFamilyServer
import cod.majestic.discord.bot.service.FamilyMemberService
import cod.majestic.discord.bot.service.PersonalBranchService
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
    private val discordRoleCache: DiscordRoleCache,
    private val personalBranchService: PersonalBranchService,
    private val familyMemberService: FamilyMemberService
) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.componentId != ComponentId.CREATE_APPLICATION_GET_TIER.getComponentId()) return

        val guild = event.guild ?: return
        val member = event.member ?: return

        val categoryGetTier = guild.categories.firstOrNull { it.name == "\uD83D\uDD2F・Заявки на получение тиров" }
        if (categoryGetTier == null) {
            event.reply("❌ Не найдена категория «Заявки на получение тиров». Сообщите администрации бота.")
                .setEphemeral(true)
                .queue()
            return
        }
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

        // Без авторизации нет записи о члене семьи, без личной ветки нечего прикладывать к заявке.
        if (!familyMemberService.existsByDiscordUserId(member.user.idLong)) {
            event.reply("❌ Вы не авторизованы на сервере. Сначала нажмите кнопку авторизации, затем повторите заявку.")
                .setEphemeral(true)
                .queue()
            return
        }

        if (!personalBranchService.existsByDiscordUserId(member.user.idLong)) {
            event.reply("❌ У вас нет личной ветки. Сначала создайте личную ветку, затем повторите заявку на тир.")
                .setEphemeral(true)
                .queue()
            return
        }

        val channelName = "\uD83D\uDEB9・заявка-на-получение-тира-${member.user.id}"
        val personalBranch = personalBranchService.findByDiscordUserId(member.user.idLong)
        val asMentionBranch = codFamilyServer.getTextChannels()
            .find { it.idLong == personalBranch.discordChannelId }
            ?.asMention ?: "временно отсутствует"

        categoryGetTier.createTextChannel(channelName)
            .addPermissionOverride(
                guild.publicRole,
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                member,
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ADD_REACTION, Permission.CREATE_PUBLIC_THREADS, Permission.CREATE_PRIVATE_THREADS, Permission.MESSAGE_SEND_IN_THREADS, Permission.MESSAGE_ATTACH_FILES),
                null
            )
            .addPermissionOverride(
                discordRoleCache.getDepLeader(),
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                null
            )
            .addPermissionOverride(
                discordRoleCache.getLeader(),
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                null
            )
            .addPermissionOverride(
                discordRoleCache.getHigh(),
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ADD_REACTION, Permission.CREATE_PUBLIC_THREADS, Permission.CREATE_PRIVATE_THREADS, Permission.MESSAGE_SEND_IN_THREADS, Permission.MESSAGE_ATTACH_FILES),
                null
            )
            .addPermissionOverride(
                discordRoleCache.getTierChecker(),
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ADD_REACTION, Permission.CREATE_PUBLIC_THREADS, Permission.CREATE_PRIVATE_THREADS, Permission.MESSAGE_SEND_IN_THREADS, Permission.MESSAGE_ATTACH_FILES),
                null
            )
            .addPermissionOverride(
                discordRoleCache.getRecruiter(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getMedia(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getTier2(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getTier1(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getWaitingCall(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getInactive(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getCod(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .queue { createdChannel ->

                val message =
                    """
                    📋 **Заявка на получение тира**

                    Запрос от пользователя: ${member.asMention}.
                    Личная ветка пользователя: $asMentionBranch
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
}