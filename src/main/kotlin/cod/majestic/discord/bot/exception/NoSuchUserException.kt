package cod.majestic.discord.bot.exception

class NoSuchUserException(
    private val description: String
) : NoSuchElementException()