package cod.majestic.discord.bot.component

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


    /**
     * Откаты
     */
    DETAILS_ANALYSE_OTKAT("details_analyse_otkat_id"),
    APPROVE_DETAILS_ANALYSE_OTKAT("approve_details_analyse_otkat_id"),

    /**
     * Регистрация
     */
    REGISTRATION("registration_id");

    fun getComponentId() = componentId

    fun createComponentId(identifierParam: String) = componentId + "_" + identifierParam

    fun getIdentifierParam(listenerEventId: String) =
        listenerEventId.removePrefix(componentId + "_").toLong()
}