package cod.majestic.discord.bot.listener.details.otkat

import cod.majestic.discord.bot.cache.DiscordRoleCache
import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.component.FieldId
import cod.majestic.discord.bot.component.ModalId
import cod.majestic.discord.bot.model.enums.RoleType
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal
import org.springframework.stereotype.Component

@Component
class DetailsAnalyseOtkatListener(
    private val discordRoleCache: DiscordRoleCache
) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.componentId != ComponentId.DETAILS_ANALYSE_OTKAT.getComponentId()) return

        val guild = event.guild ?: return
        val member = event.member ?: return

        val existingChannel = guild.textChannels
            .firstOrNull { it.name == "\uD83E\uDDE9・заявка-на-разбор-отката-${member.user.id}" }

        if (existingChannel != null) {
            event.reply("❌ У вас уже есть активная заявка: ${existingChannel.asMention}")
                .setEphemeral(true)
                .queue()
            return
        }

        val modal = Modal.create(ModalId.DETAILS_ANALYSE_OTKAT.getModalId(), ModalId.DETAILS_ANALYSE_OTKAT.getTitle())
            .addActionRows(
                ActionRow.of(
                    TextInput.create(FieldId.URL_VIDEOS.getId(), FieldId.URL_VIDEOS.getTitle(), FieldId.URL_VIDEOS.getSize())
                        .setRequired(FieldId.URL_VIDEOS.getRequired())
                        .setPlaceholder(FieldId.URL_VIDEOS.getPlaceHolder())
                        .build()
                )
            )
            .build()

        event.replyModal(modal).queue()
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if (event.modalId != ComponentId.DETAILS_ANALYSE_OTKAT.getComponentId()) return

        val guild = event.guild ?: return
        val member = event.member ?: return

        val urlVideos = event.getValue(FieldId.URL_VIDEOS.getId())?.asString!!

        val deputyRole = discordRoleCache.getDepLeader()
        val leaderRole = discordRoleCache.getLeader()
        val recruiterRole = discordRoleCache.getRecruiter()
        val mediaRole = discordRoleCache.getMedia()
        val highRole = discordRoleCache.getHigh()
        val testRole = discordRoleCache.getTest()
        val mainRole = discordRoleCache.getMain()
        val tier1Role = discordRoleCache.getTier1()
        val tier2Role = discordRoleCache.getTier2()
        val tier3Role = discordRoleCache.getTier3()

        val categoryGetRang = guild.categories.firstOrNull { it.name == "\uD83D\uDD2B・Tier"}!!
        val channelName = "\uD83E\uDDE9・заявка-на-разбор-отката-${member.id}"

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
                tier1Role,
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                tier2Role,
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                tier3Role,
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
                highRole,
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
                    📋 **Новая заявка на подробный разбор отката**

                    Пользователь: ${member.asMention}
                  
                    Просьба подробно разобрать откат(ы) ниже:
                    
                    $urlVideos
                    
                    """.trimIndent()

                createdChannel.sendMessage(
                    message
                ).setActionRow(
                    Button.success(ComponentId.DETAILS_ANALYSE_OTKAT.createComponentId(member.user.id), "Разобрано")
                ).queue()

                event.reply("Заявка создана: ${createdChannel.asMention}")
                    .setEphemeral(true)
                    .queue()
            }
    }
}