package cod.majestic.discord.bot.listener.crime.event

import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.component.ModalId
import cod.majestic.discord.bot.model.enums.EventCrimeType
import cod.majestic.discord.bot.server.CodFamilyServer
import cod.majestic.discord.bot.service.EventCrimeService
import cod.majestic.discord.bot.service.MessageService
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AddPointAndUpdateMessageListener(
    private val eventCrimeService: EventCrimeService,
    private val messageService: MessageService
) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (!event.componentId.startsWith(ComponentId.ADD_POINT_TO_MEMBER.getComponentId())) return

        val eventCrimeId = ComponentId.ADD_POINT_TO_MEMBER.getIdentifierParam(event.componentId)
        val modal = Modal.create(
            ModalId.ADD_POINT_TO_MEMBER.createModalId(eventCrimeId.toString()),
            ModalId.ADD_POINT_TO_MEMBER.getTitle()
        )
            .addActionRows(
                ActionRow.of(
                    TextInput.create(
                        "add_point_count",
                        "Введите количество баллов",
                        TextInputStyle.SHORT
                    )
                        .setRequired(true)
                        .build()
                )
            )
            .build()

        event.replyModal(modal).queue()
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if (!event.modalId.startsWith(ModalId.ADD_POINT_TO_MEMBER.getModalId())) return

        val eventCrimeId = ModalId.ADD_POINT_TO_MEMBER.getIdentifierParam<UUID>(event.modalId)
        val messageDiscordId = eventCrimeService.findById(eventCrimeId).discordMessageId
        val message = messageService.getMessageFromActivityMemberFamilyChannel(messageDiscordId)

        message.editMessageComponents(ActionRow.of(emptyList())).complete()
        message.addReaction(Emoji.fromUnicode("")).complete()
    }
}