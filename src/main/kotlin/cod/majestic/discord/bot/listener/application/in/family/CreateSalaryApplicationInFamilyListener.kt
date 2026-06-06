package cod.majestic.discord.bot.listener.application.`in`.family

import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.cache.DiscordRoleCache
import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.component.FieldId
import cod.majestic.discord.bot.component.ModalId
import cod.majestic.discord.bot.model.enums.MainChannelType
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
class CreateSalaryApplicationInFamilyListener(
    private val discordRoleCache: DiscordRoleCache,
    private val discordChannelCache: DiscordChannelCache
) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.componentId != ComponentId.CREATE_APPLICATION_IN_FAMILY_ON_SALARY.getComponentId()) return

        val guild = event.guild ?: return
        val member = event.member ?: return

        val existingChannel = guild.textChannels
            .firstOrNull { it.name == "☢\uFE0F・заявка-в-семью-платка-${member.user.id}" }

        if (existingChannel != null) {
            event.reply("❌ У вас уже есть активная заявка: ${existingChannel.asMention}")
                .setEphemeral(true)
                .queue()
            return
        }

        val modal = Modal.create(
            ModalId.CREATE_APPLICATION_IN_FAMILY_ON_SALARY.getModalId(),
            ModalId.CREATE_APPLICATION_IN_FAMILY_ON_SALARY.getTitle()
        )
            .addActionRows(
                ActionRow.of(
                    TextInput.create(FieldId.PERSON_INFO.getId(), FieldId.PERSON_INFO.getTitle(), FieldId.PERSON_INFO.getSize())
                        .setRequired(FieldId.PERSON_INFO.getRequired())
                        .setPlaceholder(FieldId.PERSON_INFO.getPlaceHolder())
                        .build()
                ),
                ActionRow.of(
                    TextInput.create(FieldId.PERSON_INFO_2.getId(), FieldId.PERSON_INFO_2.getTitle(), FieldId.PERSON_INFO.getSize())
                        .setRequired(FieldId.PERSON_INFO_2.getRequired())
                        .setPlaceholder(FieldId.PERSON_INFO_2.getPlaceHolder())
                        .build()
                ),
                ActionRow.of(
                    TextInput.create(FieldId.GAME_EXPERIENCE.getId(), FieldId.GAME_EXPERIENCE.getTitle(), FieldId.GAME_EXPERIENCE.getSize())
                        .setRequired(FieldId.GAME_EXPERIENCE.getRequired())
                        .setPlaceholder(FieldId.GAME_EXPERIENCE.getPlaceHolder())
                        .build()
                ),
                ActionRow.of(
                    TextInput.create(FieldId.LAST_FAM_INFO.getId(), FieldId.LAST_FAM_INFO.getTitle(), FieldId.LAST_FAM_INFO.getSize())
                        .setRequired(FieldId.LAST_FAM_INFO.getRequired())
                        .setPlaceholder(FieldId.LAST_FAM_INFO.getPlaceHolder())
                        .build()
                ),
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
        if (event.modalId != ModalId.CREATE_APPLICATION_IN_FAMILY_ON_SALARY.getModalId()) return

        val guild = event.guild ?: return
        val member = event.member ?: return

        val personInfo = event.getValue(FieldId.PERSON_INFO.getId())!!.asString
        val personInfoSecond = event.getValue(FieldId.PERSON_INFO_2.getId())!!.asString
        val gameExperience = event.getValue(FieldId.GAME_EXPERIENCE.getId())!!.asString
        val lastFamilies = event.getValue(FieldId.LAST_FAM_INFO.getId())!!.asString
        val urlVideos = event.getValue(FieldId.URL_VIDEOS.getId())!!.asString

        val channelName = "☢\uFE0F・заявка-в-семью-платка-${member.user.id}"
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
                    Button.success(
                        ComponentId.FAMILY_APPLICATION_ON_SALARY_APPROVED.createComponentId(member.user.id),
                        "Одобрить"
                    ),
                    Button.secondary(
                        ComponentId.FAMILY_APPLICATION_ON_SALARY_REJECT.createComponentId(member.user.id),
                        "Отклонить"
                    )
                ).queue()

                discordChannelCache.getBy(MainChannelType.ARCHIVE_APPLICATION).sendMessage(message).queue()

                event.reply("Заявка создана: ${createdChannel.asMention}")
                    .setEphemeral(true)
                    .queue()
            }
    }
}