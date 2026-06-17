package cod.majestic.discord.bot.mapper

import cod.majestic.discord.bot.model.EventCrime
import cod.majestic.discord.bot.model.enums.EventCrimeServer
import cod.majestic.discord.bot.model.projection.CrimeStatisticProjection
import cod.majestic.discord.bot.server.CodFamilyServer
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class EventCrimeMapper(
    private val codFamilyServer: CodFamilyServer
) {

    fun toDiscordMessage(listCrimeEvent: List<CrimeStatisticProjection>, days: Long): String {
        if (listCrimeEvent.isEmpty()) return "```📭 Данные отсутствуют```"

        val sb = StringBuilder()
        sb.appendLine("**📊 Статистика активности семьи:**\n")

        listCrimeEvent.forEachIndexed { index, crime ->
            val user = codFamilyServer.getUserById(crime.getDiscordUserId())
            val server = EventCrimeServer.valueOf(crime.getEventCrimeServer()).getNameServer()
            val time = crime.getLastActionTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))

            sb.appendLine("**${index + 1}.** ${user.asMention}")
            sb.appendLine("　┣ 🎮 Сервер: `$server`")
            sb.appendLine("　┣ 🎯 Количество RP отыгранное за последние $days дней: `${crime.getCountRp()}`")
            sb.appendLine("　┣ ⭐ Очки: `${crime.getPoints()}`")
            sb.appendLine("　┗ 🕐 Последняя активность: `$time`")
            sb.appendLine()
        }

        return sb.toString()
    }
}