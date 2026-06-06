package cod.majestic.discord.bot.model.enums

enum class MainChannelType(
    val nameInDiscord: String,
) {

    APPLICATION_IN_FAMILY("├・заявка-в-семью\uD83D\uDDD2\uFE0F"),
    APPLICATION_IN_FAMILY_SALARY("├・заявка-на-платную-основу\uD83D\uDCB0"),
    DETAILS_ANALYSE_OTKAT("├・заявка-на-разбор-отката\uD83D\uDCF9"),
    ACTIVITY_FAMILY_MEMBER_MAIN("└・активность-и-баллы⚔\uFE0F"),
    ACTIVITY_FAMILY_MEMBER_HISTORY("└・активность-и-баллы⚔\uFE0F"),
    REGISTRATION(""),
    GET_TIER("├・заявка-на-получение-тира⚜\uFE0F"),
    ARCHIVE_APPLICATION("┌・архив-заявлений\uD83C\uDF00")


}