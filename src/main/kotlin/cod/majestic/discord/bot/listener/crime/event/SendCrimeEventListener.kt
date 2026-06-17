package cod.majestic.discord.bot.listener.crime.event

import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.model.EventCrime
import cod.majestic.discord.bot.model.enums.EventCrimeServer
import cod.majestic.discord.bot.model.enums.EventCrimeType
import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.service.EventCrimeService
import cod.majestic.discord.bot.service.FamilyMemberService
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class SendCrimeEventListener(
    private val eventCrimeService: EventCrimeService,
    private val familyMemberService: FamilyMemberService,
    private val discordChannelCache: DiscordChannelCache
) : ListenerAdapter() {

    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        if (event.componentId != "crime_type_select") return

        val typeCrime = event.values[0]
        val modal = Modal.create("send_crime_info_event_${typeCrime}", "Информация о крайм-событии.")
            .addActionRows(
                ActionRow.of(
                    TextInput.create(
                        "crime_event_server",
                        "Сервер (цифра). Для сервера MCL укажите 0",
                        TextInputStyle.SHORT
                    )
                        .setRequired(true)
                        .build()
                ),
                ActionRow.of(
                    TextInput.create("crime_event_url", "Ссылка на видео или скриншот", TextInputStyle.PARAGRAPH)
                        .setRequired(true)
                        .build()
                )
            )
            .build()

        event.replyModal(modal).queue()
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if (!event.modalId.contains("send_crime_info_event_")) return

        event.deferReply(true).complete()

        val member = event.member ?: return

        val crimeEventTypeString = event.modalId.replace("send_crime_info_event_", "")
        val crimeEventServerString = event.getValue("crime_event_server")!!.asString
        val crimeEventUrlString = event.getValue("crime_event_url")!!.asString

        val crimeEventTypeResult = kotlin
            .runCatching { EventCrimeType.valueOf(crimeEventTypeString) }
            .onFailure { sendIncorrectTypeEvent(event, crimeEventTypeString) }
            .onSuccess { it.name }

        if (crimeEventTypeResult.isFailure) return

        val crimeEventServerResult = kotlin
            .runCatching { EventCrimeServer.findByNumber(crimeEventServerString.toInt()) }
            .onFailure { sendIncorrectFormatServer(event, crimeEventServerString) }
            .onSuccess { it.toString() }

        if (crimeEventServerResult.isFailure) return

        val channel = discordChannelCache.getBy(MainChannelType.ACTIVITY_FAMILY_MEMBER_HISTORY)

        val eventCrimeId = UUID.randomUUID()

        val buttonAccept = Button.primary(
            ComponentId.ADD_POINT_TO_MEMBER.createComponentId(eventCrimeId.toString()),
            "Начислить баллы"
        )
        val buttonCancel = Button.danger(
            ComponentId.CANCEL_ADD_POINT_TO_MEMBER.createComponentId(eventCrimeId.toString()),
            "Отказать"
        )

        val message = channel.sendMessage(
            """
            Идентификатор того, кто отправил: ${member.user.id}
            Кто отправил: ${member.asMention}
            Тип события: ${crimeEventTypeResult.getOrThrow().getDescription()}
            Сервер: ${crimeEventServerResult.getOrThrow().getNameServer()}
            Доказательство: $crimeEventUrlString
            """.trimIndent()
        )
            .setActionRow(buttonAccept, buttonCancel)
            .complete()

        val familyMember = familyMemberService.getByUserId(member.user.idLong)

        val eventCrime = EventCrime(
            id = eventCrimeId,
            type = crimeEventTypeResult.getOrThrow(),
            actionTime = LocalDateTime.now(),
            discordMessageId = message.idLong,
            familyMember = familyMember,
            server = crimeEventServerResult.getOrThrow()
        )

        eventCrimeService.save(eventCrime)

        event.hook.sendMessage("Событие отправлено на проверку.")
            .setEphemeral(true)
            .queue()
    }

    fun sendIncorrectTypeEvent(event: ModalInteractionEvent, crimeEventType: String) {
        event.hook.sendMessage("Некорректное тип крайм-события, используете ${EventCrimeType.entries.toTypedArray()}.")
            .setEphemeral(true)
            .queue()
    }

    fun sendIncorrectFormatServer(event: ModalInteractionEvent, crimeEventServer: String) {
        event.hook.sendMessage("Некорректное название или сервер который вы указали семья не играет.")
            .setEphemeral(true)
            .queue()
    }
}