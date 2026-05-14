package cod.majestic.discord.bot.listener

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
class DetailsAnalyseOtkatListener : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.componentId != "details_analyse_otkat") return

        val guild = event.guild ?: return
        val member = event.member ?: return

        val existingChannel = guild.textChannels
            .firstOrNull { it.name == "\uD83E\uDDE9・заявка-на-разбор-отката-${member.id}" }

        if (existingChannel != null) {
            event.reply("❌ У вас уже есть активная заявка: ${existingChannel.asMention}")
                .setEphemeral(true)
                .queue()
            return
        }

        val modal = Modal.create("details_analyse_otkat", "Заявка в семью")
            .addActionRows(
                ActionRow.of(
                    TextInput.create("urlVideos", "Откаты с CAPT, MCL, GG и т.д:", TextInputStyle.PARAGRAPH)
                        .setRequired(false)
                        .build()
                )
            )
            .build()

        event.replyModal(modal).queue()
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if (event.modalId != "details_analyse_otkat") return

        val guild = event.guild ?: return
        val member = event.member ?: return

        val urlVideos = event.getValue("urlVideos")?.asString!!

        val deputyRole = guild.roles.find { it.name == RoleType.DEPUTY_LEADER.nameInDiscord } ?: return
        val leaderRole = guild.roles.find { it.name == RoleType.LEADER.nameInDiscord } ?: return
        val recruiterRole = guild.roles.find { it.name == RoleType.RECRUITER.nameInDiscord } ?: return
        val mediaRole = guild.roles.find { it.name == RoleType.MEDIA.nameInDiscord } ?: return
        val highRole = guild.roles.find { it.name == RoleType.HIGH.nameInDiscord } ?: return
        val rpRole = guild.roles.find { it.name == RoleType.RP.nameInDiscord } ?: return
        val tegRole = guild.roles.find { it.name == RoleType.TEG.nameInDiscord } ?: return
        val mainRole = guild.roles.find { it.name == RoleType.MAIN.nameInDiscord } ?: return
        val mainPlusRole = guild.roles.find { it.name == RoleType.MAIN_PLUS.nameInDiscord } ?: return
        val codRole = guild.roles.find { it.name == RoleType.COD.nameInDiscord } ?: return

        val categoryGetRang = guild.categories.firstOrNull { it.name == "\uD83D\uDD2B・Повышение"}!!
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
                rpRole,
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                tegRole,
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                mainRole,
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                mainPlusRole,
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                codRole,
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
                    Button.success("approve_details_analyse_otkat_${member.id}", "Разобрано")
                ).queue()

                event.reply("Заявка создана: ${createdChannel.asMention}")
                    .setEphemeral(true)
                    .queue()
            }
    }
}