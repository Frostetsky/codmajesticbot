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
class CreateApplicationInFamilyListener : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.componentId == "create_application") {

            val guild = event.guild ?: return
            val member = event.member ?: return

            val existingChannel = guild.textChannels
                .firstOrNull { it.name == "☢\uFE0F・заявка-в-семью-${member.id}" }

            if (existingChannel != null) {
                event.reply("❌ У вас уже есть активная заявка: ${existingChannel.asMention}")
                    .setEphemeral(true)
                    .queue()
                return
            }

            val modal = Modal.create("application_modal", "Заявка в семью")
                .addActionRows(
                    ActionRow.of(
                        TextInput.create("personInfo", "Ваш возраст IRL:", TextInputStyle.SHORT)
                            .setRequired(true)
                            .setPlaceholder("Минимальный возраст 16 лет.")
                            .build()
                    ),
                    ActionRow.of(
                        TextInput.create("urlVideos", "Откаты (capt/mcl/gg):", TextInputStyle.PARAGRAPH)
                            .setRequired(true)
                            .setPlaceholder("rutube/youtube.")
                            .build()
                    ),
                    ActionRow.of(
                        TextInput.create("urlScreens", "Скрин персонажей:", TextInputStyle.PARAGRAPH)
                            .setPlaceholder("imgur/yapix/ibb")
                            .setRequired(false)
                            .build()
                    )
                )
                .build()

            event.replyModal(modal).queue()
        }
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if (event.modalId != "application_modal") return

        val guild = event.guild ?: return
        val member = event.member ?: return

        val age = event.getValue("personInfo")!!.asString
        val urlScreens = event.getValue("urlScreens")!!.asString
        val urlVideos = event.getValue("urlVideos")!!.asString

        val deputyRole = guild.roles.find { it.name == RoleType.DEPUTY_LEADER.nameInDiscord } ?: return
        val leaderRole = guild.roles.find { it.name == RoleType.LEADER.nameInDiscord } ?: return
        val highRole = guild.roles.find { it.name == RoleType.HIGH.nameInDiscord } ?: return
        val recruiterRole = guild.roles.find { it.name == RoleType.RECRUITER.nameInDiscord } ?: return

        val channelName = "☢\uFE0F・заявка-в-семью-${member.id}"
        val category = guild.categories.find { it.name == "\uD83D\uDCCB・Активные заявки" } ?: return

        category.createTextChannel(channelName)
            .addPermissionOverride(
                guild.publicRole,
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
            .addPermissionOverride(
                highRole,
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                null
            )
            .addPermissionOverride(
                recruiterRole,
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                null
            )
            .queue { createdChannel ->

                val message =
                    """
                    📋 **Новая заявка**

                    Пользователь: ${member.asMention}
                  
                    Возраст: $age
                    Скриншот персонажей: $urlScreens
                    Откаты (CAPT/MCL/GG): $urlVideos
                    
                    """.trimIndent()

                createdChannel.sendMessage(
                    message
                ).setActionRow(
                    Button.primary("waiting_call_${member.id}", "Обзвонить"),
                    Button.success("approve_application_${member.id}", "Принять"),
                    Button.secondary("reject_application_${member.id}", "Отклонить")
                ).queue()

                guild.textChannels.find { it.name == "┌・архив-заявлений\uD83C\uDF00" }?.sendMessage(message)?.queue()

                event.reply("Заявка создана: ${createdChannel.asMention}")
                    .setEphemeral(true)
                    .queue()
            }
    }
}