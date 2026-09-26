package cod.majestic.discord.bot.cache

import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.model.enums.RoleType
import cod.majestic.discord.bot.server.CodFamilyServer
import net.dv8tion.jda.api.entities.Role
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.util.*

@Component
class DiscordRoleCache(
    private val coDFamilyServer: CodFamilyServer
) : InitializingBean {

    private val cacheRole: MutableMap<RoleType, Role> = EnumMap(RoleType::class.java)

    fun getMedia() = cacheRole[RoleType.MEDIA] ?: throw NoSuchElementException(messageError(RoleType.MEDIA))

    fun getTest() = cacheRole[RoleType.TEST] ?: throw NoSuchElementException(messageError(RoleType.TEST))

    fun getRecruiter() = cacheRole[RoleType.RECRUITER] ?: throw NoSuchElementException(messageError(RoleType.RECRUITER))

    fun getDepLeader() = cacheRole[RoleType.DEPUTY_LEADER] ?: throw NoSuchElementException(messageError(RoleType.DEPUTY_LEADER))

    fun getLeader() = cacheRole[RoleType.LEADER] ?: throw NoSuchElementException(messageError(RoleType.LEADER))

    fun getHigh() = cacheRole[RoleType.HIGH] ?: throw NoSuchElementException(messageError(RoleType.HIGH))

    fun getWaitingCall() = cacheRole[RoleType.WAITING_CALL] ?: throw NoSuchElementException(messageError(RoleType.WAITING_CALL))

    fun getTier1() = cacheRole[RoleType.TIER_1] ?: throw NoSuchElementException(messageError(RoleType.TIER_1))

    fun getTier2() = cacheRole[RoleType.TIER_2] ?: throw NoSuchElementException(messageError(RoleType.TIER_2))

    fun getTier3() = cacheRole[RoleType.TIER_3] ?: throw NoSuchElementException(messageError(RoleType.TIER_3))

    fun getInactive() = cacheRole[RoleType.INACTIVE] ?: throw NoSuchElementException(messageError(RoleType.INACTIVE))

    fun getCod() = cacheRole[RoleType.COD] ?: throw NoSuchElementException(messageError(RoleType.COD))

    fun getCs() = cacheRole[RoleType.CS] ?: throw NoSuchElementException(messageError(RoleType.CS))

    fun getDota() = cacheRole[RoleType.DOTA] ?: throw NoSuchElementException(messageError(RoleType.DOTA))

    fun getTanki() = cacheRole[RoleType.TANKI] ?: throw NoSuchElementException(messageError(RoleType.TANKI))

    fun getPubg() = cacheRole[RoleType.PUBG] ?: throw NoSuchElementException(messageError(RoleType.PUBG))

    fun getMinecraft() = cacheRole[RoleType.MINECRAFT] ?: throw NoSuchElementException(messageError(RoleType.MINECRAFT))

    fun getRoblox() = cacheRole[RoleType.ROBLOX] ?: throw NoSuchElementException(messageError(RoleType.ROBLOX))


    override fun afterPropertiesSet() {
        initCache()
    }

    private fun initCache() {
        val roles = coDFamilyServer.getMyGuild().roles
        RoleType.entries.forEach { role ->
            val roleToCache = roles.find { it.name == role.nameInDiscord }
                ?: throw NoSuchElementException("Отсутствует роль %s на Discord сервере.".format(role.nameInDiscord))
            cacheRole[role] = roleToCache
        }
    }

    private fun messageError(param: RoleType): String = "Отсутствует роль %s в кеше.".format(param.nameInDiscord)
}