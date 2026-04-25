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
                .firstOrNull { it.name == "заявка-в-семью-${member.id}" }

            if (existingChannel != null) {
                event.reply("❌ У вас уже есть активная заявка: ${existingChannel.asMention}")
                    .setEphemeral(true)
                    .queue()
                return
            }

            val modal = Modal.create("application_modal", "Заявка в семью")
                .addActionRows(
                    ActionRow.of(
                        TextInput.create("personInfo", "Реальное имя, никнейм, static ID, возраст:", TextInputStyle.SHORT)
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
                        TextInput.create("expectationsFromFamily", "Твои ожидания от семьи?", TextInputStyle.PARAGRAPH)
                            .setRequired(true)
                            .build()
                    ),
                    ActionRow.of(
                        TextInput.create("urlVideos", "Откаты с capt, mcl, mp, gg (если имеются):", TextInputStyle.PARAGRAPH)
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

        val name = event.getValue("personInfo")!!.asString
        val gameExperience = event.getValue("gameExperience")!!.asString
        val lastFamilies = event.getValue("lastFamilies")!!.asString
        val expectationsFromFamily = event.getValue("expectationsFromFamily")!!.asString
        val urlVideos = event.getValue("urlVideos")?.asString ?: ""

        val deputyRole = guild.roles.find { it.name == RoleType.DEPUTY_LEADER.nameInDiscord } ?: return
        val leaderRole = guild.roles.find { it.name == RoleType.LEADER.nameInDiscord } ?: return
        val highRole = guild.roles.find { it.name == RoleType.HIGH.nameInDiscord } ?: return

        val channelName = "заявка-в-семью-${member.user.name}".lowercase()

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
            .queue { createdChannel ->

                val message =
                    """
                    📋 **Новая заявка**

                    Пользователь: ${member.asMention}
                  
                    Реальное имя, игровой никнейм, staticID, возраст:: $name
                    Опыт игры на Majestic RP (по времени) и средний онлайн: $gameExperience
                    В каких семьях был? Причины ухода? $lastFamilies
                    Твои ожидания от семьи? $expectationsFromFamily
                    Откаты с CAPT, MCL, MP, GG: $urlVideos
                    
                    """.trimIndent()

                createdChannel.sendMessage(
                    message
                ).setActionRow(
                    Button.success("approve_application_${member.id}", "Одобрить"),
                    Button.secondary("reject_application_${member.id}", "Отклонить")
                ).queue()

                guild.textChannels.find { it.name == "\uD83C\uDF00・архив-заявок" }?.sendMessage(message)?.queue()

                event.reply("Заявка создана: ${createdChannel.asMention}")
                    .setEphemeral(true)
                    .queue()
            }
    }
}