package cod.majestic.discord.bot.listener.warn

import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.component.FieldId
import cod.majestic.discord.bot.component.ModalId
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.modals.Modal
import org.springframework.stereotype.Component
import java.util.*

@Component
class RemoveWarnListener() : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (!event.componentId.startsWith(ComponentId.REMOVE_WARN.getComponentId())) return

        val userId = event.user.id
        val modal = Modal.create(
            ModalId.REMOVE_WARN.createModalId(userId),
            ModalId.REMOVE_WARN.getTitle()
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

}