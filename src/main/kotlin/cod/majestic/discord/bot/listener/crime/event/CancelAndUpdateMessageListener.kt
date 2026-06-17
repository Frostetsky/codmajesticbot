package cod.majestic.discord.bot.listener.crime.event

import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.component.FieldId
import cod.majestic.discord.bot.component.ModalId
import cod.majestic.discord.bot.service.EventCrimeService
import cod.majestic.discord.bot.service.MessageService
import cod.majestic.discord.bot.utils.getRequiredLongValue
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component
import java.util.*

@Component
class CancelAndUpdateMessageListener(
    private val eventCrimeService: EventCrimeService,
    private val messageService: MessageService
) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (!event.componentId.startsWith(ComponentId.CANCEL_ADD_POINT_TO_MEMBER.getComponentId())) return

        event.deferReply(true).complete()

        val eventCrimeId = ComponentId.CANCEL_ADD_POINT_TO_MEMBER.getIdentifierParam<UUID>(event.componentId)
        val eventCrime = eventCrimeService.findById(eventCrimeId)
        val messageDiscordId = eventCrime.discordMessageId
        val message = messageService.getMessageFromActivityMemberFamilyChannel(messageDiscordId)

        message
            .editMessage(message.contentRaw)
            .setComponents(listOf())
            .complete()
            .addReaction(Emoji.fromUnicode("U+274C"))
            .complete()

        event.hook.setEphemeral(true)
            .sendMessage("Члену семьи отказано в начислении баллов за ${eventCrime.type.getDescription()}")
            .complete()
    }
}