package cod.majestic.discord.bot.listener.admin.strategy

import cod.majestic.discord.bot.model.enums.AdminCommand

interface CommandHandler<RQ> {

    fun execute(body: RQ): String

    fun command(): AdminCommand
}