package cod.majestic.discord.bot.utils

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

fun ModalInteractionEvent.getRequiredLongValue(id: String): Long =
    this.getValue(id)?.asString?.toLong() ?: throw NoSuchElementException("Long value not found by id %s.".format(id))

/**
 * Режет длинный текст на части, подходящие под лимит Discord (2000 символов), по границам строк.
 */
fun String.splitForDiscord(limit: Int = 1900): List<String> {
    if (this.length <= limit) return listOf(this)

    val chunks = mutableListOf<String>()
    val current = StringBuilder()

    for (line in this.lines()) {
        val parts = if (line.length > limit) line.chunked(limit) else listOf(line)
        for (part in parts) {
            if (current.isNotEmpty() && current.length + part.length + 1 > limit) {
                chunks.add(current.toString())
                current.clear()
            }
            if (current.isNotEmpty()) current.append('\n')
            current.append(part)
        }
    }
    if (current.isNotEmpty()) chunks.add(current.toString())
    return chunks
}

/**
 * Пытается отправить ЛС. В callback приходит true, если сообщение доставлено (у пользователя могут быть закрыты ЛС).
 */
fun net.dv8tion.jda.api.entities.User.trySendDm(text: String, callback: (Boolean) -> Unit = {}) {
    this.openPrivateChannel().queue(
        { channel -> channel.sendMessage(text).queue({ callback(true) }, { callback(false) }) },
        { callback(false) }
    )
}
