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
class CreateApplicationInFamilyListener(
    private val discordRoleCache: DiscordRoleCache,
    private val discordChannelCache: DiscordChannelCache
) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.componentId != ComponentId.CREATE_APPLICATION_IN_FAMILY.getComponentId()) return

        val guild = event.guild ?: return
        val member = event.member ?: return

        val existingChannel = guild.textChannels
            .firstOrNull { it.name == "☢\uFE0F・заявка-в-семью-${member.user.id}" }

        if (existingChannel != null) {
            event.reply("❌ У вас уже есть активная заявка: ${existingChannel.asMention}")
                .setEphemeral(true)
                .queue()
            return
        }

        val modal = Modal.create(
            ModalId.CREATE_APPLICATION_IN_FAMILY.getModalId(),
            ModalId.CREATE_APPLICATION_IN_FAMILY.getTitle()
        )
            .addActionRows(
                ActionRow.of(
                    TextInput.create(FieldId.AGE.getId(), FieldId.AGE.getTitle(), FieldId.AGE.getSize())
                        .setRequired(FieldId.AGE.getRequired())
                        .setPlaceholder(FieldId.AGE.getPlaceHolder())
                        .build()
                ),
                ActionRow.of(
                    TextInput.create(FieldId.URL_VIDEOS.getId(), FieldId.URL_VIDEOS.getTitle(), FieldId.URL_VIDEOS.getSize())
                        .setRequired(FieldId.URL_VIDEOS.getRequired())
                        .setPlaceholder(FieldId.URL_VIDEOS.getPlaceHolder())
                        .build()
                ),
                ActionRow.of(
                    TextInput.create(FieldId.SCREEN_PERSON.getId(), FieldId.SCREEN_PERSON.getTitle(), FieldId.SCREEN_PERSON.getSize())
                        .setRequired(FieldId.SCREEN_PERSON.getRequired())
                        .setPlaceholder(FieldId.SCREEN_PERSON.getPlaceHolder())
                        .build()
                )
            )
            .build()

        event.replyModal(modal).queue()
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if (event.modalId != ModalId.CREATE_APPLICATION_IN_FAMILY.getModalId()) return

        val guild = event.guild ?: return
        val member = event.member ?: return

        val age = event.getValue(FieldId.AGE.getId())!!.asString
        val urlScreens = event.getValue(FieldId.URL_VIDEOS.getId())!!.asString
        val urlVideos = event.getValue(FieldId.SCREEN_PERSON.getId())!!.asString

        val channelName = "☢\uFE0F・заявка-в-семью-${member.user.id}"
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
            .addPermissionOverride(
                discordRoleCache.getRecruiter(),
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
                    Button.primary(
                        ComponentId.GIVE_WAITING_CALL.createComponentId(member.user.id),
                        "Обзвонить"
                    ),
                    Button.success(
                        ComponentId.FAMILY_APPLICATION_APPROVED.createComponentId(member.user.id),
                        "Принять"
                    ),
                    Button.secondary(
                        ComponentId.FAMILY_APPLICATION_REJECT.createComponentId(member.user.id),
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