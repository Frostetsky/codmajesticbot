package cod.majestic.discord.bot.model.enums

enum class RoleType(
    val nameInDiscord: String
) {

    LEADER("LEADER"),
    DEPUTY_LEADER("DEPUTY LEADER"),
    HIGH("HIGH"),
    RECRUITER("RECRUIT"),
    MEDIA("MEDIA"),
    MAIN("MAIN"),
    TIER_1("TIER 1"),
    TIER_2("TIER 2"),
    TIER_3("TIER 3"),
    TEST("TEST"),
    INACTIVE("INACTIVE"),
    COD("COD"),
    P_1("P1"),
    WAITING_CALL("ОЖИДАНИЕ ОБЗВОНА");

    companion object {
        fun findByDiscordName(nameInDiscord: String): RoleType {
            return RoleType.entries.find { it.nameInDiscord == nameInDiscord } ?: throw IllegalStateException("Unsupported roleServer $nameInDiscord")
        }
    }
}