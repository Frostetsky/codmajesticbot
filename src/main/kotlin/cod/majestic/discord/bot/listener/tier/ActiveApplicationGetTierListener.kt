package cod.majestic.discord.bot.listener.tier

import cod.majestic.discord.bot.cache.DiscordRoleCache
import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.component.FieldId
import cod.majestic.discord.bot.component.ModalId
import cod.majestic.discord.bot.model.enums.RoleType
import cod.majestic.discord.bot.utils.trySendDm
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.modals.Modal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Component
class ActiveApplicationGetTierListener : ListenerAdapter() {

    private lateinit var discordRoleCache: DiscordRoleCache

    private enum class TierAction(
        val button: ComponentId,
        val modal: ModalId,
        val label: String
    ) {
        TIER_1(ComponentId.APPROVE_TIER_1, ModalId.APPROVE_TIER_1, "TIER 1"),
        TIER_2(ComponentId.APPROVE_TIER_2, ModalId.APPROVE_TIER_2, "TIER 2")
    }

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        val action = TierAction.entries.firstOrNull { event.componentId.startsWith(it.button.getComponentId()) } ?: return
        val member = event.member ?: return

        if (!hasTierPermissions(member)) {
            event.reply("Недостаточно прав.").setEphemeral(true).queue()
            return
        }

        val targetId = action.button.getIdentifierParam<Long>(event.componentId)

        val modal = Modal.create(
            action.modal.createModalId(targetId.toString()),
            action.modal.getTitle()
        )
            .addActionRows(
                ActionRow.of(
                    TextInput.create(
                        FieldId.TIER_COMMENT.getId(),
                        FieldId.TIER_COMMENT.getLabel(),
                        FieldId.TIER_COMMENT.getSize()
                    )
                        .setRequired(FieldId.TIER_COMMENT.getRequired())
                        .setPlaceholder(FieldId.TIER_COMMENT.getPlaceHolder())
                        .setMaxLength(1000)
                        .build()
                )
            )
            .build()

        event.replyModal(modal).queue()
    }

    /** Отправка модалки -> выдача роли, ЛС с комментарием, удаление канала заявки. */
    override fun onModalInteraction(event: ModalInteractionEvent) {
        val action = TierAction.entries.firstOrNull { event.modalId.startsWith(it.modal.getModalId()) } ?: return
        val guild = event.guild ?: return
        val moderator = event.member ?: return

        if (!hasTierPermissions(moderator)) {
            event.reply("Недостаточно прав.").setEphemeral(true).queue()
            return
        }

        event.deferReply(true).complete()

        val targetId = action.modal.getIdentifierParam<Long>(event.modalId)
        val comment = event.getValue(FieldId.TIER_COMMENT.getId())?.asString?.trim().orEmpty()

        val targetMember = runCatching { guild.retrieveMemberById(targetId).complete() }.getOrNull()
        if (targetMember == null) {
            event.hook.sendMessage("❌ Пользователь не найден на сервере, роль не выдана.")
                .setEphemeral(true)
                .queue()
            return
        }

        val (roleToAdd, rolesToRemove) = rolesFor(action)
        guild.modifyMemberRoles(targetMember, listOf(roleToAdd), rolesToRemove).queue()

        val dmText = buildString {
            appendLine("✅ Тебе выдан **${action.label}**!")
            appendLine()
            appendLine("Выдал: ${moderator.effectiveName}")
            if (comment.isNotBlank()) append("Комментарий: $comment")
        }.trim()

        val channel = event.channel?.asTextChannel()

        targetMember.user.trySendDm(dmText) { delivered ->
            val status = if (delivered) {
                "✅ ${action.label} выдан ${targetMember.asMention}, комментарий отправлен в ЛС."
            } else {
                "✅ ${action.label} выдан ${targetMember.asMention}, но ЛС отправить не удалось (закрыты личные сообщения)."
            }
            event.hook.sendMessage(status).setEphemeral(true).queue(
                { channel?.delete()?.queue() },
                { channel?.delete()?.queue() }
            )
        }
    }

    private fun rolesFor(action: TierAction): Pair<Role, List<Role>> = when (action) {
        TierAction.TIER_1 -> discordRoleCache.getTier1() to listOf(discordRoleCache.getTier2())
        TierAction.TIER_2 -> discordRoleCache.getTier2() to emptyList()
    }

    private fun hasTierPermissions(member: Member): Boolean {
        return member.roles.any {
            it.name == RoleType.LEADER.nameInDiscord
                    || it.name == RoleType.DEPUTY_LEADER.nameInDiscord
                    || it.name == RoleType.TIER_CHECKER.nameInDiscord
        }
    }

    @Autowired
    @Lazy
    fun setDiscordChannelCache(discordRoleCache: DiscordRoleCache) {
        this.discordRoleCache = discordRoleCache
    }
}
