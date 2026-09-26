package cod.majestic.discord.bot.model.enums

enum class MainChannelType(
    val nameInDiscord: String,
) {

    APPLICATION_IN_FAMILY("├・\uD83D\uDCD1заявка-в-семью\uD83D\uDCD1"),
    DETAILS_ANALYSE_OTKAT("├・заявка-на-разбор-отката\uD83D\uDCF9"),
    ACTIVITY_FAMILY_MEMBER_MAIN("└・активность-и-баллы⚔\uFE0F"),
    ACTIVITY_FAMILY_MEMBER_HISTORY("├・активность-членов-семьи✈\uFE0F"),
    AUTHORIZATION("├・\uD83C\uDFC1-авторизация-\uD83C\uDFC1"),
    GET_TIER("├・заявка-на-получение-тира⚜\uFE0F"),
    ARCHIVE_APPLICATION("┌・\uD83D\uDDC3\uFE0Fархив-заявлений\uD83D\uDDC3\uFE0F"),
    ADMINISTRATION("┌・администрирование\uD83D\uDFE3"),
    INACTIVE("├・\uD83D\uDE34неактивность\uD83D\uDE34"),
    CREATE_PERSONAL_BRANCH("├・создание-личной-ветки♎"),
    ADD_OTHER_GAMES_ROLES("├・\uD83D\uDCA5-получение-ролей\uD83D\uDCA5"),
    CAPT_PLUS("├・➕\uD83C\uDDE8\uD83C\uDDE6\uD83C\uDDF5\uD83C\uDDF9-\uD83C\uDDF5\uD83C\uDDF1\uD83C\uDDFA\uD83C\uDDF8➕"),
}