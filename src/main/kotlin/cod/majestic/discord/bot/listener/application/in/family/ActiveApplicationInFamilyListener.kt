package cod.majestic.discord.bot.listener.application.`in`.family

import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.cache.DiscordRoleCache
import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.component.FieldId
import cod.majestic.discord.bot.component.ModalId
import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.model.enums.RoleType
import cod.majestic.discord.bot.utils.trySendDm
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.modals.Modal
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ActiveApplicationInFamilyListener(
    private val discordRoleCache: DiscordRoleCache,
    private val discordChannelCache: DiscordChannelCache
) : ListenerAdapter() {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun onButtonInteraction(event: ButtonInteractionEvent) {

        val guild = event.guild ?: return
        val member = event.member ?: return
        val channel = event.channel.asTextChannel()

        when {

            event.componentId.startsWith(ComponentId.FAMILY_APPLICATION_REJECT.getComponentId()) -> {
                if (!hasLeaderPermissions(member)) {
                    sendPermissionError(event)
                    return
                }

                val targetId = ComponentId.FAMILY_APPLICATION_REJECT.getIdentifierParam<Long>(event.componentId)

                val modal = Modal.create(
                    ModalId.REJECT_APPLICATION_IN_FAMILY.createModalId(targetId.toString()),
                    ModalId.REJECT_APPLICATION_IN_FAMILY.getTitle()
                )
                    .addActionRows(
                        ActionRow.of(
                            TextInput.create(
                                FieldId.REJECT_REASON.getId(),
                                FieldId.REJECT_REASON.getLabel(),
                                FieldId.REJECT_REASON.getSize()
                            )
                                .setRequired(FieldId.REJECT_REASON.getRequired())
                                .setPlaceholder(FieldId.REJECT_REASON.getPlaceHolder())
                                .setMaxLength(1000)
                                .build()
                        )
                    )
                    .build()

                event.replyModal(modal).queue()
            }

            event.componentId.startsWith(ComponentId.FAMILY_APPLICATION_APPROVED.getComponentId()) -> {
                if (!hasLeaderPermissions(member)) {
                    sendPermissionError(event)
                    return
                }

                val targetId = ComponentId.FAMILY_APPLICATION_APPROVED.getIdentifierParam<Long>(event.componentId)
                val targetMember = guild.retrieveMemberById(targetId).complete()

                guild.modifyMemberRoles(
                    targetMember,
                    listOf(discordRoleCache.getTest(), discordRoleCache.getCod(), discordRoleCache.getPp()),
                    listOf(discordRoleCache.getWaitingCall())
                ).queue()

                channel.delete().queue()
            }

            event.componentId.startsWith(ComponentId.GIVE_WAITING_CALL.getComponentId()) -> {
                if (!hasLeaderPermissions(member)) {
                    sendPermissionError(event)
                    return
                }

                val targetId = ComponentId.GIVE_WAITING_CALL.getIdentifierParam<Long>(event.componentId)
                val targetMember = guild.retrieveMemberById(targetId).complete()

                val waitingCallRole = discordRoleCache.getWaitingCall()
                val waitingChannel = guild.channels.find { it.name == "├・ожидание-обзвона◀\uFE0F" }

                guild.addRoleToMember(targetMember, waitingCallRole).queue()

                targetMember.user.openPrivateChannel().queue { privateChannel ->
                    privateChannel.sendMessage(
                        """
                        ✅ Твоя заявка рассмотрена и одобрена!
            
                        Теперь ты находишься в списке на ожидание обзвона.
                        Пожалуйста, перейди в канал ${waitingChannel?.asMention} и ожидай дальнейших инструкций.
                        """.trimIndent()
                    ).queue {
                        event.reply("✅ Пользователь ${targetMember.asMention} уведомлён в личные сообщения.")
                            .setEphemeral(true)
                            .queue()
                    }
                }
            }
        }
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if (!event.modalId.startsWith(ModalId.REJECT_APPLICATION_IN_FAMILY.getModalId())) return

        val guild = event.guild ?: return
        val moderator = event.member ?: return

        if (!hasLeaderPermissions(moderator)) {
            event.reply("Недостаточно прав.").setEphemeral(true).queue()
            return
        }

        event.deferReply(true).complete()

        val targetId = ModalId.REJECT_APPLICATION_IN_FAMILY.getIdentifierParam<Long>(event.modalId)
        val reason = event.getValue(FieldId.REJECT_REASON.getId())?.asString?.trim().orEmpty()
            .ifBlank { "Причина не указана" }
        val channel = event.channel?.asTextChannel()

        // Снимаем «ожидание обзвона» (если пользователь ещё на сервере).
        runCatching { guild.retrieveMemberById(targetId).complete() }.getOrNull()?.let {
            guild.removeRoleFromMember(it, discordRoleCache.getWaitingCall()).queue()
        }

        // Архив заявлений.
        writeRejectToArchive(targetId, moderator, reason)

        // ЛС пользователю.
        val dmText = """
            ❌ Твоя заявка в семью отклонена.
            
            **Причина:** $reason
            """.trimIndent()

        event.jda.retrieveUserById(targetId).queue(
            { user ->
                user.trySendDm(dmText) { delivered ->
                    finishReject(event, channel, delivered)
                }
            },
            { finishReject(event, channel, false) }
        )
    }

    private fun finishReject(event: ModalInteractionEvent, channel: net.dv8tion.jda.api.entities.channel.concrete.TextChannel?, dmDelivered: Boolean) {
        val status = if (dmDelivered) {
            "✅ Заявка отклонена. Причина отправлена пользователю в ЛС и записана в архив."
        } else {
            "⚠️ Заявка отклонена и записана в архив, но отправить ЛС не удалось (закрыты личные сообщения или пользователь покинул сервер)."
        }
        event.hook.sendMessage(status).setEphemeral(true).queue(
            { channel?.delete()?.queue() },
            { channel?.delete()?.queue() }
        )
    }

    /**
     * Пишет отказ в архив заявлений. Если исходное сообщение заявки находится в последних 100 сообщениях архива —
     * отвечает на него (reply), иначе отправляет отдельное сообщение.
     */
    private fun writeRejectToArchive(targetId: Long, moderator: Member, reason: String) {
        val archive = discordChannelCache.getBy(MainChannelType.ARCHIVE_APPLICATION)
        val text = """
            ❌ **Заявка отклонена**
            
            Пользователь: <@$targetId>
            Отклонил: ${moderator.asMention}
            Причина: $reason
            """.trimIndent()

        runCatching {
            val original: Message? = archive.history.retrievePast(100).complete()
                .firstOrNull { it.contentRaw.contains("Новая заявка") && it.contentRaw.contains("<@$targetId>") }

            val action = original?.reply(text) ?: archive.sendMessage(text)
            action.setAllowedMentions(emptySet()).queue()
        }.onFailure { log.error("Не удалось записать отказ в архив заявлений", it) }
    }

    private fun hasLeaderPermissions(member: Member): Boolean {
        return member.roles.any {
            it.name == RoleType.LEADER.nameInDiscord
                    || it.name == RoleType.DEPUTY_LEADER.nameInDiscord
                    || it.name == RoleType.HIGH.nameInDiscord
                    || it.name == RoleType.RECRUITER.nameInDiscord
        }
    }

    private fun sendPermissionError(buttonInteractionEvent: ButtonInteractionEvent) {
        buttonInteractionEvent.reply("Недостаточно прав.")
            .setEphemeral(true)
            .queue()
    }
}
