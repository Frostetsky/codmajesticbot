package cod.majestic.discord.bot.model.enums

enum class RoleType(
    val nameInDiscord: String
) {

    LEADER("LEADER"),
    DEPUTY_LEADER("DEPUTY LEADER"),
    HIGH("HIGH"),
    RECRUITER("RECRUIT"),
    MEDIA("MEDIA"),
    TIER_CHECKER("TIER CHECKER"),
    TIER_1("TIER 1"),
    TIER_2("TIER 2"),
    INACTIVE("INACTIVE"),
    COD("COD"),
    PP("P-P"),

    WAITING_CALL("ОЖИДАНИЕ ОБЗВОНА");

    companion object {
        fun findByDiscordName(nameInDiscord: String): RoleType {
            return RoleType.entries.find { it.nameInDiscord == nameInDiscord } ?: throw IllegalStateException("Unsupported roleServer $nameInDiscord")
        }
    }
}