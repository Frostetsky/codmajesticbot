package cod.majestic.discord.bot.listener.inactive

import cod.majestic.discord.bot.cache.DiscordRoleCache
import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.component.FieldId
import cod.majestic.discord.bot.component.ModalId
import cod.majestic.discord.bot.model.InactiveMember
import cod.majestic.discord.bot.server.CodFamilyServer
import cod.majestic.discord.bot.service.InactiveMemberService
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.modals.Modal
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class MoveToInactiveListener(
    private val discordRoleCache: DiscordRoleCache,
    private val codFamilyServer: CodFamilyServer,
    private val inactiveMemberService: InactiveMemberService
) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (!event.componentId.startsWith(ComponentId.MOVE_TO_INACTIVE.getComponentId())) return

        val userId = ComponentId.MOVE_TO_INACTIVE.getIdentifierParam<Long>(event.componentId)
        val modal = Modal.create(
            ModalId.MOVE_TO_INACTIVE.createModalId(userId.toString()),
            ModalId.MOVE_TO_INACTIVE.getTitle()
        )
            .addActionRows(
                ActionRow.of(
                    TextInput.create(FieldId.DESCRIPTION_TO_INACTIVE.getId(), FieldId.DESCRIPTION_TO_INACTIVE.getLabel(), FieldId.DESCRIPTION_TO_INACTIVE.getSize())
                        .setPlaceholder(FieldId.DESCRIPTION_TO_INACTIVE.getPlaceHolder())
                        .setRequired(FieldId.DESCRIPTION_TO_INACTIVE.getRequired())
                        .build()
                )
            )
            .build()

        event.replyModal(modal).queue()
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if (!event.modalId.startsWith(ModalId.MOVE_TO_INACTIVE.getModalId())) return

        event.deferReply(true).complete()

        val guild = codFamilyServer.getMyGuild()
        val userId = ModalId.MOVE_TO_INACTIVE.getIdentifierParam<Long>(event.modalId)
        val description = event.getValue(FieldId.DESCRIPTION_TO_INACTIVE.getId()).toString()

        val targetMember = codFamilyServer.getUserById(userId)
        val inactiveRole = discordRoleCache.getInactive()

        guild.addRoleToMember(targetMember, inactiveRole).complete()

        val inactiveMember = InactiveMember(
            id = UUID.randomUUID(),
            discordUserId = userId,
            description = description,
            actionTime = LocalDateTime.now(),
        )

        inactiveMemberService.save(inactiveMember)

        event.hook.setEphemeral(true)
            .sendMessage("Вы перешли в статус неактивности.")
            .complete()
    }
}