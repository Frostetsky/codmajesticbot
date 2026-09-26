package cod.majestic.discord.bot.mapper

import cod.majestic.discord.bot.dto.AddPointToMemberRequest
import cod.majestic.discord.bot.dto.GetLastFamilyRequest
import cod.majestic.discord.bot.model.InactiveMember
import cod.majestic.discord.bot.model.enums.EventCrimeServer
import cod.majestic.discord.bot.model.enums.EventCrimeType
import cod.majestic.discord.bot.model.projection.CrimeStatisticProjection
import cod.majestic.discord.bot.server.CodFamilyServer
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class AdministrationMapper(
    private val codFamilyServer: CodFamilyServer,
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH:mm")
) {

    fun toGetLastFamilyRequest(splitsString: Array<String>): GetLastFamilyRequest {
        val limit = splitsString[1].toInt()
        val days = splitsString[2].toLong()
        return GetLastFamilyRequest(limit, days)
    }

    fun toAddPointToMemberRequest(splitsString: Array<String>, whoUserAsMention: String): AddPointToMemberRequest {
        val asMentionUser = splitsString[1]
        val typeRp = EventCrimeType.valueOf(splitsString[2])
        val points = splitsString[3].toLong()
        val actionTime = LocalDateTime.parse(splitsString[4], dateTimeFormatter)
        return AddPointToMemberRequest(asMentionUser, typeRp, points, actionTime, whoUserAsMention)
    }


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

    fun toDiscordMessage(inactiveMembers: Iterable<InactiveMember>): String {
        if (!inactiveMembers.iterator().hasNext()) return "```📭 Данные отсутствуют```"

        val sb = StringBuilder()
        sb.appendLine("**📊 Неактивные члены семьи на текущий момент:**\n")

        inactiveMembers.forEachIndexed { index, inactiveMember ->
            val user = codFamilyServer.getUserById(inactiveMember.discordUserId)

            sb.appendLine("**${index + 1}.** ${user.asMention}")
            sb.appendLine("　┣ \uD83D\uDCCB Причина: `${inactiveMember.description}`")
            sb.appendLine("　┣ \uD83D\uDD50 Время ухода в неактив: `${inactiveMember.actionTime}`")
            sb.appendLine()
        }

        return sb.toString()
    }
}