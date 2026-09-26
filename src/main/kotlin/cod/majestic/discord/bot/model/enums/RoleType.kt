package cod.majestic.discord.bot.model.enums

enum class RoleType(
    val nameInDiscord: String
) {

    LEADER("LEADER"),
    DEPUTY_LEADER("DEPUTY LEADER"),
    HIGH("HIGH"),
    RECRUITER("RECRUIT"),
    MEDIA("MEDIA"),
    TIER_1("TIER 1"),
    TIER_2("TIER 2"),
    TIER_3("TIER 3"),
    TEST("TEST"),
    INACTIVE("INACTIVE"),
    COD("COD"),
    CS("CS"),
    DOTA("DOTA"),
    PUBG("PUBG"),
    MINECRAFT("MINECRAFT"),
    ROBLOX("ROBLOX"),
    TANKI("TANKI"),

    WAITING_CALL("ОЖИДАНИЕ ОБЗВОНА");

    companion object {
        fun findByDiscordName(nameInDiscord: String): RoleType {
            return RoleType.entries.find { it.nameInDiscord == nameInDiscord } ?: throw IllegalStateException("Unsupported roleServer $nameInDiscord")
        }
    }
}