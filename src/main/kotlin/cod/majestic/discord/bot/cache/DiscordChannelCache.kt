package cod.majestic.discord.bot.cache

import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.server.CodFamilyServer
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.util.*

@Component
class DiscordChannelCache(
    private val codFamilyServer: CodFamilyServer
) : InitializingBean {

    private val channelCache: MutableMap<MainChannelType, TextChannel> = EnumMap(MainChannelType::class.java)

    fun getBy(mainChannelType: MainChannelType): TextChannel =
        channelCache[mainChannelType] ?: throw NoSuchElementException(messageError(mainChannelType))

    override fun afterPropertiesSet() {
        initCache()
    }

    private fun initCache() {
        val channels = codFamilyServer.getTextChannels()
        MainChannelType.entries.forEach { channel ->
            val channelToCache = channels.find { it.name == channel.nameInDiscord }
                ?: throw NoSuchElementException("Отсутствует канал %s на Discord сервере.".format(channel.nameInDiscord))
            channelCache[channel] = channelToCache
        }
    }

    private fun messageError(param: MainChannelType): String = "Отсутствует канал %s в кеше.".format(param.nameInDiscord)
}