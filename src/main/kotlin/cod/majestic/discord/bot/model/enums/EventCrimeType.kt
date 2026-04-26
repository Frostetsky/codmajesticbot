package cod.majestic.discord.bot.model.enums

enum class EventCrimeType(
    val description: String
) {

    CAPT("Капт"),
    MCL("MCL"),
    GG("Ган-гейм"),
    STRELA("Стрела"),
    VZZ("ВЗЗ"),
    VZM("ВЗМ"),
    FZ("ФЗ"),
    OSTROV("Остров"),
    CEHA("Цеха"),
    DILLERS("Диллеры"),
    POSTAVKA("Поставка"),
    DROP("Дроп"),
    TAINIKI("Тайники");

    fun getRp() = listOf(CEHA, DILLERS, POSTAVKA, DROP, TAINIKI, FZ, OSTROV)

}