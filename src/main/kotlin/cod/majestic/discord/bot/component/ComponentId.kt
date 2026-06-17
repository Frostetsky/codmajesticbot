package cod.majestic.discord.bot.component

import java.util.*

enum class ComponentId(
    private val componentId: String
) {

    /**
     * Тиры
     */
    CREATE_APPLICATION_GET_TIER("create_application_get_tier_id"),
    APPROVE_TIER_1("approve_tier_1_id"),
    APPROVE_TIER_2("approve_tier_2_id"),
    APPROVE_TIER_3("approve_tier_3_id"),

    /**
     * Заявки в семью
     */
    CREATE_APPLICATION_IN_FAMILY("create_application_in_family_id"),
    CREATE_APPLICATION_IN_FAMILY_ON_SALARY("create_application_in_family_on_salary_id"),
    FAMILY_APPLICATION_ON_SALARY_APPROVED("salary_approve_application_in_family_id"),
    FAMILY_APPLICATION_ON_SALARY_REJECT("salary_reject_application_in_family_id"),
    FAMILY_APPLICATION_APPROVED("approve_application_in_family_id"),
    FAMILY_APPLICATION_REJECT("reject_application_in_family_id"),
    GIVE_WAITING_CALL("give_waiting_call_id"),

    /**
     * Баллы
     */
    GET_MY_POINTS("get_count_my_points_id"),
    ADD_POINT_TO_MEMBER("add_point_to_member_id"),
    CANCEL_ADD_POINT_TO_MEMBER("cancel_add_point_to_member_id"),

    /**
     * Откаты
     */
    DETAILS_ANALYSE_OTKAT("details_analyse_otkat_id"),
    APPROVE_DETAILS_ANALYSE_OTKAT("approve_details_analyse_otkat_id"),

    /**
     * Авторизация
     */
    AUTHORIZATION("authorization_id"),

    /**
     * Крайм-события
     */
    SEND_CRIME_EVENT("send_crime_event_id"),

    /**
     * Активность и неактивность
     */
    MOVE_TO_INACTIVE("move_to_inactive_id"),
    MOVE_TO_ACTIVE("move_to_active_id"),

    /**
     * Предупреждения
     */
    REMOVE_WARN("remove_warn_id");

    fun getComponentId() = componentId

    fun createComponentId(identifierParam: String) = componentId + "_" + identifierParam

    inline fun <reified T> getIdentifierParam(listenerEventId: String): T {
        return when (T::class) {
            UUID::class -> UUID.fromString(listenerEventId.removePrefix(getComponentId() + "_")) as T
            Long::class -> listenerEventId.removePrefix(getComponentId() + "_").toLong() as T
            else -> throw IllegalArgumentException("Unsupported type: ${T::class}")
        }
    }
}