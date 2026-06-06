package cod.majestic.discord.bot.component

import java.util.*

enum class ModalId(
    private val modalId: String,
    private val title: String,
) {

    CREATE_APPLICATION_IN_FAMILY("create_application_modal_salary_id", "Заявка в семью."),
    CREATE_APPLICATION_IN_FAMILY_ON_SALARY("create_application_modal_salary_on_salary_id", "Заявка в семью на платную основу."),

    DETAILS_ANALYSE_OTKAT("details_analyse_otkat_id", "Запрос на разбор отката."),
    ADD_POINT_TO_MEMBER("add_point_to_member_id", "Выдаваемое количество баллов.");


    fun getModalId() = modalId
    fun getTitle() = title

    fun createModalId(identifierParam: String) = getModalId() + "_" + identifierParam

    inline fun <reified T> getIdentifierParam(listenerEventId: String): T {
        return when (T::class) {
            UUID::class -> UUID.fromString(listenerEventId.removePrefix(getModalId() + "_")) as T
            Long::class -> listenerEventId.removePrefix(getModalId() + "_").toLong() as T
            else -> throw IllegalArgumentException("Unsupported type: ${T::class}")
        }
    }
}