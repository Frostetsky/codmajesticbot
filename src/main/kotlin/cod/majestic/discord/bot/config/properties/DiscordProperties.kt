package cod.majestic.discord.bot.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "discord")
class DiscordProperties(
    val token: String
)