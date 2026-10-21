package cod.majestic.discord.bot.model.enums

enum class AdminCommand(
    /** Роли, которым разрешена команда. Пустое множество — ограничений по ролям нет. */
    val allowedRoles: Set<RoleType> = emptySet()
) {

    GET_ACTIVE_OF_MEMBER,

    ADD_POINT_TO_MEMBER,

    GET_INACTIVE_MEMBER,

    CLEAR_MESSAGE,

    DELETE_PERSONAL_BRANCH(
        setOf(RoleType.TIER_CHECKER, RoleType.HIGH, RoleType.DEPUTY_LEADER, RoleType.LEADER)
    )
}
