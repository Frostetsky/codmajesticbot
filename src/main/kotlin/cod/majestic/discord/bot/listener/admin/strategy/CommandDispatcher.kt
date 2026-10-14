package cod.majestic.discord.bot.listener.admin.strategy

import cod.majestic.discord.bot.dto.AddPointToMemberRequest
import cod.majestic.discord.bot.dto.ClearMessageRequest
import cod.majestic.discord.bot.dto.GetInactiveMemberRequest
import cod.majestic.discord.bot.dto.GetLastFamilyRequest
import cod.majestic.discord.bot.mapper.AdministrationMapper
import cod.majestic.discord.bot.model.enums.AdminCommand
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.springframework.stereotype.Component

@Component
class CommandDispatcher(
    private val commandMap: Map<AdminCommand, CommandHandler<*>>,
    private val administrationMapper: AdministrationMapper
) {

    @Suppress("UNCHECKED_CAST")
    fun execute(messageReceived: MessageReceivedEvent): Pair<TextChannel, String> {
        // \s+ вместо " ": устойчиво к двойным пробелам и переносам строк.
        val splitMessage = messageReceived.message.contentRaw.trim().split(Regex("\\s+")).toTypedArray()
        val command = AdminCommand.valueOf(splitMessage[0].uppercase())
        val commandHandler = commandMap[command] ?: throw IllegalArgumentException("Unknown command")

        return when (command) {
            AdminCommand.GET_ACTIVE_OF_MEMBER ->
                (commandHandler as CommandHandler<GetLastFamilyRequest>)
                    .execute(administrationMapper.toGetLastFamilyRequest(splitMessage))

            AdminCommand.ADD_POINT_TO_MEMBER ->
                (commandHandler as CommandHandler<AddPointToMemberRequest>)
                    .execute(administrationMapper.toAddPointToMemberRequest(splitMessage, messageReceived.author.asMention))

            AdminCommand.GET_INACTIVE_MEMBER ->
                (commandHandler as CommandHandler<GetInactiveMemberRequest>)
                    .execute(GetInactiveMemberRequest())

            AdminCommand.CLEAR_MESSAGE ->
                (commandHandler as CommandHandler<ClearMessageRequest>)
                    .execute(administrationMapper.toClearMessageRequest(splitMessage))
        }
    }
}
