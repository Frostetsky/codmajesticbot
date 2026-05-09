package cod.majestic.discord.bot.service.button.impl

import cod.majestic.discord.bot.service.button.CreateApplicationInFamilyService
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import java.awt.Color

@Service
class CreateApplicationInFamilyServiceImpl(
    private val jda: JDA
) : CreateApplicationInFamilyService, InitializingBean {

    override fun createAndSendButton() {
        val guild = jda.guilds.first()
        val channel = guild.textChannels.find {
            it.name.contains("├・заявка-в-семью\uD83D\uDDD2\uFE0F")
        } ?: return

        channel.history.retrievePast(20).queue { messages ->
            val alreadyExists = messages.any { message -> isBot(message.author.id, jda.selfUser.id) }

            if (!alreadyExists) {
                val embed = EmbedBuilder()
                    .setTitle("📝 Создание заявки в семью")
                    .setDescription("""
                        **Нажмите кнопку ниже и заполните форму**
                        
                      
                        **📌 Примечания:**
                        
                        • Если у вас нет каких-либо откатов вашего игрового процесса, вы можете претендовать только на ранг "RP" и катать только РП контент от семьи. Вписываться в МКЛ и КАПТ составы вы не будете.
                        
                        • Если у вас есть какие-либо откаты, вы сразу можете претендовать на центральный ранг в нашей семье.
                        
                        • Цвет семьи - розовый. Нижняя часть тела - любого цвета.
                        
                        • ⚠️ **Строго 15+ лет.**
                        
                    
                        *Нажмите на кнопку ниже, чтобы начать оформление заявки*
                    """.trimIndent()
                    )
                    .setColor(Color.PINK)
                    .setImage("https://media.discordapp.net/attachments/1497345512024572059/1497345513656422521/aiclip_644ce557fe5941d99d906162583cb9b1.gif?ex=69ef296d&is=69edd7ed&hm=b25dc76798405df3c117f8be8253679fbb136879b99ede4baa4807c3398df715&")
                    .build()
                channel.sendMessageEmbeds(embed)
                    .setActionRow(
                        Button.primary("create_application", "Создать заявку в семью")
                    ).queue()
            }
        }
    }

    fun isBot(messageUserId: String, botUserId: String) = messageUserId == botUserId

    override fun afterPropertiesSet() {
        createAndSendButton()
    }
}