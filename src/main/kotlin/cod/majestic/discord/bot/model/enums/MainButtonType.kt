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
                        
                        • Цвет семьи - розовый. Нижняя часть тела - любого цвета.
                        
                        • ⚠️ **Строго 15+ лет.**
                        
                    
                        *Нажмите на кнопку ниже, чтобы начать оформление заявки*
                    """.trimIndent()
            )
            .setColor(Color.PINK)
            .setImage("https://media.discordapp.net/attachments/1497345512024572059/1497345513656422521/aiclip_644ce557fe5941d99d906162583cb9b1.gif?ex=69ef296d&is=69edd7ed&hm=b25dc76798405df3c117f8be8253679fbb136879b99ede4baa4807c3398df715&")
            .build(),
        MainChannelType.APPLICATION_IN_FAMILY
    ),

    CREATE_APPLICATION_ON_SALARY_IN_FAMILY(
        "create_application_in_family_on_salary_id",
        "Создать заявку в семью на платную основу",
        EmbedBuilder()
            .setTitle("📝 Создание заявки в семью на платную основу")
            .setDescription("""
                        **Нажмите кнопку ниже и заполните форму**
                        
                      
                        **📌 Требования:**
                        
                        1. 1.8 K/D минимум. Сервер 14+. Минимум 4 отката с ган-гейма, минимум 10 скринов с ган-гейма с закрытым лобби.
                        
                        2. Минимум 5 откатов с каптов, минимум 5 откатов с MCL/ВЗЗ. (Минимальное количество ошибок при рассмотрении)
                        
                        3. Ведение личной ветки.
                        
                        4. Присутствие на 80% мероприятиях семьи в неделю, включая RP контент.
                        
                        • ⚠️ **Строго 15+ лет.**
                        
                        **📌 От нас:**
                        
                        1. Оффер (RUB/Крипта): На основе заявки, оплата каждые 2 недели, испытательный срок неделя, увелечение оплаты в зависимости от перехода на конкретный сервер.
                        
                        2. Дополнительные ежемесячные поощрения в зависимости от результатов.
                        
                        3. Хороший коллектив.
                        
                        4. Предоставляем стартер и 20 кг рюкзак на нашем сервере.
                        
                        5. Предоставляем стартер и 20 кг рюкзак при переходе на новый сервер.
                    
                        *Нажмите на кнопку ниже, чтобы начать оформление заявки*
                    """.trimIndent()
            )
            .setColor(Color.PINK)
            .setImage("https://media.discordapp.net/attachments/1497345512024572059/1497345513656422521/aiclip_644ce557fe5941d99d906162583cb9b1.gif?ex=69ef296d&is=69edd7ed&hm=b25dc76798405df3c117f8be8253679fbb136879b99ede4baa4807c3398df715&")
            .build(),
        MainChannelType.APPLICATION_IN_FAMILY_SALARY
    ),

    SEND_CRIME_EVENT(
        "send_crime_event_id",
        "Отправить информацию о крайм-событии",
        EmbedBuilder()
            .setTitle("")
            .build(),
        MainChannelType.ACTIVITY_FAMILY_MEMBER_MAIN
    ),

    GET_COUNT_MY_POINTS(
        "",
        "",
        EmbedBuilder()
            .setTitle("")
            .build(),
        MainChannelType.GET
    ),

    DETAILS_OTKAT(
        "details_analyse_otkat_id",
        "",
        EmbedBuilder()
            .setTitle("")
            .build(),
        MainChannelType.DETAILS_ANALYSE_OTKAT
    ),

    GET_TIER(
        "get_tier_id",
        "Создать заявку на получения тира",
        EmbedBuilder()
            .setTitle("")
            .setColor(Color.PINK)
            .build(),
        MainChannelType.GET_TIER
    ),

    REGISTRATION(
        "registration_id",
        "Зарегистрироваться на сервере",
        EmbedBuilder()
            .setTitle("")
            .build(),
        MainChannelType.REGISTRATION
    )
}