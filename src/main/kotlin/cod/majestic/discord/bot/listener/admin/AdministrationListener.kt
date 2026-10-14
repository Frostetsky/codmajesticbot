package cod.majestic.discord.bot.listener.admin

import cod.majestic.discord.bot.listener.admin.strategy.CommandDispatcher
import cod.majestic.discord.bot.model.enums.AdminCommand
import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.utils.splitForDiscord
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AdministrationListener(
    private val commandDispatcher: CommandDispatcher
) : ListenerAdapter() {

    private val log = LoggerFactory.getLogger(javaClass)

    /** Команды, чей ответ содержит упоминания, но пинговать всех подряд не нужно. */
    private val silentMentionCommands = setOf(
        AdminCommand.GET_INACTIVE_MEMBER,
        AdminCommand.GET_ACTIVE_OF_MEMBER,
        AdminCommand.CLEAR_MESSAGE
    )

    override fun onMessageReceived(event: MessageReceivedEvent) {
        // Игнорируем ботов (в т.ч. самого себя): раньше ответ бота в этом же канале пытался разобраться как команда.
        if (event.author.isBot || !event.isFromGuild) return
        if (event.channel.name != MainChannelType.ADMINISTRATION.nameInDiscord) return

        val commandName = event.message.contentRaw.trim().split(Regex("\\s+")).firstOrNull()?.uppercase() ?: return
        val command = AdminCommand.entries.firstOrNull { it.name == commandName } ?: return

        try {
            val (channel, text) = commandDispatcher.execute(event)

            // Discord режет сообщения > 2000 символов — шлём частями, чтобы большие списки не пропадали молча.
            text.splitForDiscord().forEach { chunk ->
                val action = channel.sendMessage(chunk)
                if (command in silentMentionCommands) action.setAllowedMentions(emptySet())
                action.complete()
            }
        } catch (e: Exception) {
            log.error("Ошибка выполнения админ-команды {}", commandName, e)

            val reason = if (e is IllegalArgumentException || e is NoSuchElementException) e.message else null
            event.channel.sendMessage(
                "❌ Не удалось выполнить `$commandName`." + (reason?.let { "\n$it" } ?: " Подробности в логах бота.")
            ).queue()
        }
    }
}
