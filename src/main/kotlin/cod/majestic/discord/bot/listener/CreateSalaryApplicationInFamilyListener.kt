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
class CreateSalaryApplicationInFamilyListener : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.componentId == "create_application_salary") {

            val guild = event.guild ?: return
            val member = event.member ?: return

            val existingChannel = guild.textChannels
                .firstOrNull { it.name == "заявка-в-семью-платная-основа-${member.id}" }

            if (existingChannel != null) {
                event.reply("❌ У вас уже есть активная заявка: ${existingChannel.asMention}")
                    .setEphemeral(true)
                    .queue()
                return
            }

            val modal = Modal.create("application_modal_salary", "Заявка в семью")
                .addActionRows(
                    ActionRow.of(
                        TextInput.create("personInfo", "Имя, возраст, nickname:", TextInputStyle.SHORT)
                            .setRequired(true)
                            .build()
                    ),
                    ActionRow.of(
                        TextInput.create("personInfoSecond", "Main-server, staticID:", TextInputStyle.SHORT)
                            .setRequired(true)
                            .build()
                    ),
                    ActionRow.of(
                        TextInput.create("gameExperience", "Опыт игры на Majestic RP и средний онлайн:", TextInputStyle.SHORT)
                            .setRequired(true)
                            .build()
                    ),
                    ActionRow.of(
                        TextInput.create("lastFamilies", "В каких семьях был? Причины ухода?", TextInputStyle.PARAGRAPH)
                            .setRequired(true)
                            .build()
                    ),
                    ActionRow.of(
                        TextInput.create("urlVideos", "Откаты с CAPT, MCL, GG и т.д:", TextInputStyle.PARAGRAPH)
                            .setRequired(false)
                            .build()
                    )
                )
                .build()

            event.replyModal(modal).queue()
        }

    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if (event.modalId != "application_modal_salary") return

        val guild = event.guild ?: return
        val member = event.member ?: return

        val personInfo = event.getValue("personInfo")!!.asString
        val personInfoSecond = event.getValue("personInfoSecond")!!.asString
        val gameExperience = event.getValue("gameExperience")!!.asString
        val lastFamilies = event.getValue("lastFamilies")!!.asString
        val urlVideos = event.getValue("urlVideos")!!.asString

        val deputyRole = guild.roles.find { it.name == RoleType.DEPUTY_LEADER.nameInDiscord } ?: return
        val leaderRole = guild.roles.find { it.name == RoleType.LEADER.nameInDiscord } ?: return
        val highRole = guild.roles.find { it.name == RoleType.HIGH.nameInDiscord } ?: return
        val recruiterRole = guild.roles.find { it.name == RoleType.RECRUITER.nameInDiscord } ?: return

        val channelName = "заявка-в-семью-платная-основа-${member.id}"

        guild.createTextChannel(channelName)
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
                    📋 **Новая заявка на платную основу**

                    Пользователь: ${member.asMention}
                  
                    Имя, возраст, nickname: $personInfo
                    Main-server, staticID: $personInfoSecond
                    Опыт игры на Majestic RP (по времени) и средний онлайн: $gameExperience
                    В каких семьях был? Причины ухода? $lastFamilies
                    Откаты с CAPT, MCL, GG и т.д: $urlVideos
                    
                    """.trimIndent()

                createdChannel.sendMessage(
                    message
                ).setActionRow(
                    Button.success("salary_approve_application_${member.id}", "Одобрить"),
                    Button.secondary("salary_reject_application_${member.id}", "Отклонить")
                ).queue()

                guild.textChannels.find { it.name == "\uD83C\uDF00・архив-заявок" }?.sendMessage(message)?.queue()

                event.reply("Заявка создана: ${createdChannel.asMention}")
                    .setEphemeral(true)
                    .queue()
            }
    }
}