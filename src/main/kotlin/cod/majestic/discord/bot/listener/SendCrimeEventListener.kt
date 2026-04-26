package cod.majestic.discord.bot.listener

import cod.majestic.discord.bot.model.enums.EventCrimeServer
import cod.majestic.discord.bot.model.enums.EventCrimeType
import cod.majestic.discord.bot.model.enums.RoleType
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal
import org.springframework.stereotype.Component

@Component
class SendCrimeEventListener : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.componentId == "send_crime_event") {

            val modal = Modal.create("send_crime_info_event", "Информация о крайм-событии.")
                .addActionRows(
                    ActionRow.of(
                        TextInput.create("crime_event_type", "Тип события (MCL, VZZ, CAPT, GG, RP, VZM)", TextInputStyle.SHORT)
                            .setRequired(true)
                            .build()
                    ),
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
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if (event.modalId != "send_crime_info_event") return

        val guild = event.guild ?: return
        val member = event.member ?: return

        val crimeEventTypeString = event.getValue("crime_event_type")!!.asString
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

        val roles = guild.roles

        val deputyRole = roles.find { it.name == RoleType.DEPUTY_LEADER.nameInDiscord } ?: return
        val leaderRole = roles.find { it.name == RoleType.LEADER.nameInDiscord } ?: return
        val HIGHRole = roles.find { it.name == RoleType.HIGH.nameInDiscord } ?: return

        val category = guild.categories.find { it.name == "\uD83D\uDD12・Личные ветки" } ?: return

        val sanitizeUserName = sanitizeUsername(member.user.name)

        val personalBranchChannel = guild.textChannels.find { extractUsernameFromChannel(it.name) == sanitizeUserName }

        if (personalBranchChannel == null) {
            val channelName = "\uD83D\uDEB9・личная-ветка-${sanitizeUserName}"
            category.createTextChannel(channelName)
                .addPermissionOverride(
                    guild.publicRole,
                    null,
                    listOf(Permission.VIEW_CHANNEL)
                )
                .addPermissionOverride(
                    member,
                    listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_SEND_IN_THREADS, Permission.CREATE_PUBLIC_THREADS, Permission.CREATE_PRIVATE_THREADS),
                    null
                )
                .addPermissionOverride(
                    deputyRole,
                    listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ADD_REACTION),
                    null
                )
                .addPermissionOverride(
                    leaderRole,
                    listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ADD_REACTION),
                    null
                )
                .addPermissionOverride(
                    HIGHRole,
                    listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ADD_REACTION),
                    null
                )
                .queue { createdChannel ->

                    createdChannel.sendMessage(
                        """
                    📋 **Событие ${crimeEventTypeResult.getOrNull()}**

                    Сервер: ${crimeEventServerResult.getOrNull()?.getNameServer()}
                    Ссылка: $crimeEventUrlString
                    
                    """.trimIndent()
                    ).queue()

                    event.reply("Событие создано.")
                        .setEphemeral(true)
                        .queue()
                }
        } else {
            personalBranchChannel.sendMessage(
                """
                    📋 **Событие ${crimeEventTypeResult.getOrNull()}**

                    Сервер: ${crimeEventServerResult.getOrNull()?.getNameServer()}
                    Ссылка: $crimeEventUrlString
             
                    """.trimIndent()
            ).queue()

            event.reply("Событие создано.")
                .setEphemeral(true)
                .queue()
        }
    }

    fun sendIncorrectTypeEvent(event: ModalInteractionEvent, crimeEventType: String) {
        event.reply("Некорректное тип крайм-события, используете (MCL, VZZ, CAPT, GG, RP, VZM).")
            .setEphemeral(true)
            .queue()
    }

    fun sendIncorrectFormatServer(event: ModalInteractionEvent, crimeEventServer: String) {
        event.reply("Некорректное название или сервер который вы указали семья не играет.")
            .setEphemeral(true)
            .queue()
    }

    /**
     * Удаляет все запрещенные символы из username для использования в названии текстового канала
     * Разрешены: a-z, A-Z, 0-9, _
     * Все остальные символы (включая . , - пробелы и т.д.) удаляются
     */
    private fun sanitizeUsername(username: String): String = username.replace("[^a-zA-Z0-9_]".toRegex(), "")

    /**
     * Извлекает username из имени канала формата "личная-ветка-{username}"
     *
     * @param channelName Имя канала (например, "личная-ветка-userofrq")
     * @return Извлеченный username или null, если формат не совпадает
     */
    private fun extractUsernameFromChannel(channelName: String): String? {

        val prefix = "\uD83D\uDEB9・личная-ветка-"

        if (channelName.startsWith(prefix)) {
            return channelName.substring(prefix.length)
        }

        return null;
    }
}