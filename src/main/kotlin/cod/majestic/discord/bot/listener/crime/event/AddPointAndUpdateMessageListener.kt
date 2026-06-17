package cod.majestic.discord.bot.listener.crime.event

import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.component.FieldId
import cod.majestic.discord.bot.component.ModalId
import cod.majestic.discord.bot.model.FamilyMember
import cod.majestic.discord.bot.model.enums.EventCrimeType
import cod.majestic.discord.bot.server.CodFamilyServer
import cod.majestic.discord.bot.service.EventCrimeService
import cod.majestic.discord.bot.service.FamilyMemberService
import cod.majestic.discord.bot.service.MessageService
import cod.majestic.discord.bot.utils.getRequiredLongValue
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
    private val messageService: MessageService,
    private val familyMemberService: FamilyMemberService
) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (!event.componentId.startsWith(ComponentId.ADD_POINT_TO_MEMBER.getComponentId())) return

        val eventCrimeId = ComponentId.ADD_POINT_TO_MEMBER.getIdentifierParam<UUID>(event.componentId)
        val modal = Modal.create(
            ModalId.ADD_POINT_TO_MEMBER.createModalId(eventCrimeId.toString()),
            ModalId.ADD_POINT_TO_MEMBER.getTitle()
        )
            .addActionRows(
                ActionRow.of(
                    TextInput.create(FieldId.POINT.getId(), FieldId.POINT.getLabel(), FieldId.POINT.getSize())
                        .setPlaceholder(FieldId.POINT.getPlaceHolder())
                        .setRequired(FieldId.POINT.getRequired())
                        .build()
                )
            )
            .build()

        event.replyModal(modal).queue()
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if (!event.modalId.startsWith(ModalId.ADD_POINT_TO_MEMBER.getModalId())) return

        event.deferReply(true).complete()

        val eventCrimeId = ModalId.ADD_POINT_TO_MEMBER.getIdentifierParam<UUID>(event.modalId)
        val pointAdded = event.getRequiredLongValue(FieldId.POINT.getId())
        val eventCrime = eventCrimeService.findById(eventCrimeId)
        val messageDiscordId = eventCrime.discordMessageId
        val familyMember = eventCrime.familyMember
        val message = messageService.getMessageFromActivityMemberFamilyChannel(messageDiscordId)

        familyMember.apply {
            points += pointAdded
        }

        familyMemberService.save(familyMember)

        message.editMessage(
            "${message.contentRaw}\n" +
            "\n" +
            "Зачисленное количество баллов {$pointAdded}."
        )
            .setComponents(listOf())
            .complete()
            .addReaction(Emoji.fromUnicode("U+2705"))
            .complete()

        event.hook.setEphemeral(true)
            .sendMessage("Члену семьи начислено $pointAdded баллов за ${eventCrime.type.getDescription()}")
            .complete()
    }
}