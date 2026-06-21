package cod.majestic.discord.bot.listener.admin.strategy

import cod.majestic.discord.bot.dto.AddPointToMemberRequest
import cod.majestic.discord.bot.dto.GetInactiveMemberRequest
import cod.majestic.discord.bot.dto.GetLastFamilyRequest
import cod.majestic.discord.bot.mapper.AdministrationMapper
import cod.majestic.discord.bot.model.enums.AdminCommand
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import org.springframework.stereotype.Component
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel

@Component
class CommandDispatcher(
    private val commandMap: Map<AdminCommand, CommandHandler<*>>,
    private val administrationMapper: AdministrationMapper
) {

    fun execute(messageReceived: MessageReceivedEvent): Pair<TextChannel, String> {
        val splitMessage = messageReceived.message.contentRaw.split(" ").toTypedArray()
        val command = AdminCommand.valueOf(splitMessage[0])
        val commandHandler = commandMap[command] ?: throw IllegalArgumentException("Unknown command")
        return when (command) {
            AdminCommand.GET_ACTIVE_OF_MEMBER -> {
                @Suppress("UNCHECKED_CAST")
                (commandHandler as CommandHandler<GetLastFamilyRequest>)
                    .execute(administrationMapper.toGetLastFamilyRequest(splitMessage))
            }
            AdminCommand.ADD_POINT_TO_MEMBER -> {
                @Suppress("UNCHECKED_CAST")
                (commandHandler as CommandHandler<AddPointToMemberRequest>)
                    .execute(administrationMapper.toAddPointToMemberRequest(splitMessage, messageReceived.author.asMention))
            }

            AdminCommand.GET_INACTIVE_MEMBER -> {
                (commandHandler as CommandHandler<GetInactiveMemberRequest>)
                    .execute(GetInactiveMemberRequest())
            }
        }
    }
}