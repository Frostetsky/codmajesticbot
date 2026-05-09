package cod.majestic.discord.bot.service.button.impl

import cod.majestic.discord.bot.service.button.CreateSalaryApplicationInFamilyService
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import java.awt.Color

@Service
class CreateSalaryApplicationInFamilyServiceImpl(
    private val jda: JDA
) : CreateSalaryApplicationInFamilyService, InitializingBean {

    override fun createAndSendButton() {
        val guild = jda.guilds.first()
        val channel = guild.textChannels.find {
            it.name.contains("├・заявка-на-платную-основу\uD83D\uDCB0")
        } ?: return

        channel.history.retrievePast(20).queue { messages ->
            val alreadyExists = messages.any { message -> isBot(message.author.id, jda.selfUser.id) }

            if (!alreadyExists) {
                val embed = EmbedBuilder()
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
                    .build()
                channel.sendMessageEmbeds(embed)
                    .setActionRow(
                        Button.primary("create_application_salary", "Создать заявку в семью на платную основу")
                    ).queue()
            }
        }
    }

    fun isBot(messageUserId: String, botUserId: String) = messageUserId == botUserId

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}