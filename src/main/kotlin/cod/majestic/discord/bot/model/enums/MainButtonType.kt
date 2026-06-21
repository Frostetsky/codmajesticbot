package cod.majestic.discord.bot.model.enums

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.awt.Color

enum class MainButtonType(
    val id: String,
    val label: String,
    val message: MessageEmbed,
    val discordRelationChannel: MainChannelType
) {

    CREATE_APPLICATION_IN_FAMILY(
        "create_application_in_family_id",
        "Создать заявку в семью",
        EmbedBuilder()
            .setTitle("📝 Создание заявки в семью")
            .setDescription("""
                        **Нажмите кнопку ниже и заполните форму**
                        
                      
                        **📌 Примечания:**
                        
                        • Если у вас нет каких-либо откатов вашего игрового процесса, вы можете претендовать только на ранг "TEST" и катать только РП контент от семьи. Вписываться в МКЛ и КАПТ составы вы будете в редких случаях.
                        
                        • Если у вас есть какие-либо откаты, вы сразу можете претендовать на центральный ранг в нашей семье.
                        
                        • Цвет семьи - красный. Нижняя часть тела - любого цвета.
                        
                        • ⚠️ **Строго 15+ лет.**
                        
                    
                        *Нажмите на кнопку ниже, чтобы начать оформление заявки*
                    """.trimIndent()
            )
            .setColor(Color.PINK)
            .setImage("https://media.discordapp.net/attachments/1497345512024572059/1497345513656422521/aiclip_644ce557fe5941d99d906162583cb9b1.gif?ex=69ef296d&is=69edd7ed&hm=b25dc76798405df3c117f8be8253679fbb136879b99ede4baa4807c3398df715&")
            .build(),
        MainChannelType.APPLICATION_IN_FAMILY
    ),

    SEND_CRIME_EVENT(
        "send_crime_event_id",
        "Отправить информацию о крайм-событии",
        EmbedBuilder()
            .setTitle("Информация о крайм-событии")
            .build(),
        MainChannelType.ACTIVITY_FAMILY_MEMBER_MAIN
    ),

    REMOVE_WARN(
        "remove_warn_id",
        "Снять предупреждение(я)",
        EmbedBuilder()
            .setTitle("Снять предупреждение(я). 1 предупреждение - ")
            .setColor(Color.RED)
            .build(),
        MainChannelType.ACTIVITY_FAMILY_MEMBER_MAIN
    ),

    GET_COUNT_MY_POINTS(
        "get_count_my_points_id",
        "Получить количество своих баллов",
        EmbedBuilder()
            .setTitle("Получить моё текущее количество баллов")
            .setColor(Color.RED)
            .build(),
        MainChannelType.ACTIVITY_FAMILY_MEMBER_MAIN
    ),

    DETAILS_OTKAT(
        "details_analyse_otkat_id",
        "Создать заявку на детальный разбор отката",
        EmbedBuilder()
            .setTitle("Создание заявки на детальный разбор отката \uD83D\uDCF9")
            .setDescription(
                """
                        
                **В данном канале вы можете создать заявку на детальный разбор отката.**
                
                Для чего это нужно и почему мы иногда не успеваем посмотреть и разобрать ваши откаты? 📌
                
                Представьте ситуацию, за сутки могут играться 5 каптов, MCL / ВЗЗ + семейные мероприятия.
                
                1 капт в среднем длится 15 минут, 5 каптов это 75 минут, капты играют 35 человек, 35 человек по 75 минут это 2625 минут реального времени или 43 часа. Таким образом, чтобы разобрать все ваши откаты требуется почти двое суток безперерывного процесса просмотра. И это только капты.
                
                Помимо каптов у хай-состава есть игровые обязанности: решение конфликтов, разбор проблем и контроль актива семьи. Хай-состав физически может не успевать посмотреть ваш игровой процесс.
                
                Если вы заинтересованы в собственном развитии вы можете создать отдельную заявку на разбор.
                
                Данная заявка будет рассмотрена в приоритет обычного мониторинга вашей личной ветки.
                """.trimIndent()
            )
            .setColor(Color.RED)
            .build(),
        MainChannelType.DETAILS_ANALYSE_OTKAT
    ),

    GET_TIER(
        "create_application_get_tier_id",
        "Создать заявку на получения тира",
        EmbedBuilder()
            .setTitle("Создание заявки на получение тира в семье \uD83C\uDF1F")
            .setDescription(
                """
                        
                **В данном канале вы можете создать заявку на получение тира.**
                
                В нашей семье существуют 3 тира (TIER 1, TIER 2, TIER 3)
                
                • TIER 1 - вписываются в каждое мп, а также на каждый капт
                • TIER 2 -  вписываются если есть нехватка людей на мп, аналогично также и на капт
                • TIER 3 - почти никогда не вписываются на мп, иногда вписываются на капты

                Для получения %s вам необходимо:
                • 2 отката с GG (1 специальный карабин MK2 / тяжелая винтовка) и (1 тяжелый дробовик)
                • 1 откат с РП-мероприятия
                • Получить одобрение хай-состава

                Для получения %s вам необходимо:
                • 2 отката с CAPT
                • 2 отката с MCL / ВЗЗ и пройти проверку на залазы (необходимы откаты всех популярных залазов)
                • Получить одобрение хай-состава

                Для получения %s вам необходимо:
                • 3 отката с CAPT
                • 3 отката с MCL / ВЗЗ
                • Получить одобрение хай-состава

                ⚠️Откаты должны быть не старше 3-ёх недель⚠️

                Проверки проводит %s
                
                Заявки рассматриваются в течение 2-ух рабочих дней.
                """.trimIndent()
            )
            .setColor(Color.RED)
            .build(),
        MainChannelType.GET_TIER
    ),

    AUTHORIZATION(
        "authorization_id",
        "Авторизоваться на нашем сервере",
        EmbedBuilder()
            .setTitle("Авторизация на сервере")
            .setDescription("""
                Для активной работы с многим функционалом нашего дискорда вам необходимо авторизоваться.
                Нажмите кнопку ниже. Спасибо 🥰
            """.trimIndent())
            .setColor(Color.RED)
            .build(),
        MainChannelType.AUTHORIZATION
    ),

    MOVE_TO_INACTIVE(
        "move_to_inactive_id",
        "Уйти в неактив",
        EmbedBuilder()
            .setTitle("Уйти в неактив")
            .setColor(Color.RED)
            .build(),
        MainChannelType.INACTIVE
    ),

    MOVE_TO_ACTIVE(
        "move_to_active_id",
        "Вернуться из неактива",
        EmbedBuilder()
            .setTitle("Вернуться из неактива")
            .setColor(Color.RED)
            .build(),
        MainChannelType.INACTIVE
    ),

    CREATE_PERSONAL_BRANCH(
        "create_personal_branch_id",
        "Создать личную ветку для CAPT / MCL / ВЗЗ",
        EmbedBuilder()
            .setTitle("Создать личную ветку для CAPT / MCL / ВЗЗ")
            .setDescription("""
                        
                **В данном канале вы можете создать свою личную ветку.**
                
                • В названии личной ветки используйте свой игровой ник (только имя). Пример - nagi,puga,slim
                • В личную ветку необходимо заливать откаты только с CAPT / MCL / ВЗЗ
                • Проверка личных веток не всегда происходит своевременно.
                • Если вам нужно разобрать ваш откат в приоритет, используйте %s
                
                В случае успешного создания у вас появится ветка с названием 'личная-ветка-{nick}'

                """.trimIndent()
            )
            .setColor(Color.RED)
            .build(),
        MainChannelType.INACTIVE
    ),

    MOVE_TO_HOLIDAY(
        "move_to_holiday_id",
        "Уйти в отпуск",
        EmbedBuilder()
            .setTitle("Взять отпуск")
            .setColor(Color.RED)
            .build(),
        MainChannelType.INACTIVE
    )
}