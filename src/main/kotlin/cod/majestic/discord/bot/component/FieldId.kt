package cod.majestic.discord.bot.component

import net.dv8tion.jda.api.interactions.components.text.TextInputStyle

enum class FieldId(
    private val id: String,
    private val label: String,
    private val required: Boolean,
    private val placeHolder: String,
    private val size: TextInputStyle
) {

    /**
     * Предупреждения
     */
    WARN_COUNT("warnCount", "Количество предупреждений для снятия (1 или 2)", true, "1 или 2", TextInputStyle.SHORT),

    /**
     * Заявки в семью (платные)
     */
    PERSON_INFO("personInfo", "Имя, возраст, nickname:", true, "Имя, возраст, nickname", TextInputStyle.SHORT),
    PERSON_INFO_2("personInfoSecond", "Main-server, staticID:", true, "Сервер, staticID", TextInputStyle.SHORT),
    GAME_EXPERIENCE("gameExperience", "Опыт игры на Majestic RP и средний онлайн:", true, "Опыт и средний онлайн", TextInputStyle.SHORT),
    LAST_FAM_INFO("lastFamilies", "В каких семьях был? Причины ухода?", true, "Коротко описать", TextInputStyle.PARAGRAPH),


    /**
     * Заявки в семью (обычные)
     */
    AGE("age", "Ваш возраст IRL:", true, "Минимальный возраст 15 лет", TextInputStyle.SHORT),
    URL_VIDEOS("urlVideos", "Откаты (capt/mcl/gg):", true, "rutube/youtube", TextInputStyle.PARAGRAPH),
    SCREEN_PERSON("urlPersonScreens", "Скрин персонажей:", true, "imgur/yapix/ibb", TextInputStyle.PARAGRAPH),

    DESCRIPTION_TO_INACTIVE("descriptionInactive", "Причина:", true, "Описание", TextInputStyle.PARAGRAPH),

    /**
     * Баллы
     */
    POINT("add_point_count", "Введите количество баллов", true, "0", TextInputStyle.SHORT);

    fun getId() = id
    fun getLabel() = label
    fun getRequired() = required
    fun getPlaceHolder() = placeHolder
    fun getSize() = size
}