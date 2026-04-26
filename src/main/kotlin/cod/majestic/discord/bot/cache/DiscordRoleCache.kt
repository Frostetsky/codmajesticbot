package cod.majestic.discord.bot.cache

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

    fun getCoD() = cacheRole[RoleType.COD] ?: throw NoSuchElementException(messageError(RoleType.COD))

    fun getMain() = cacheRole[RoleType.MAIN] ?: throw NoSuchElementException(messageError(RoleType.MAIN))

    fun getMainPlus() = cacheRole[RoleType.MAIN_PLUS] ?: throw NoSuchElementException(messageError(RoleType.MAIN_PLUS))

    fun getRp() = cacheRole[RoleType.RP] ?: throw NoSuchElementException(messageError(RoleType.RP))

    fun getTeg() = cacheRole[RoleType.TEG] ?: throw NoSuchElementException(messageError(RoleType.TEG))

    fun getDepLeader() = cacheRole[RoleType.DEPUTY_LEADER] ?: throw NoSuchElementException(messageError(RoleType.DEPUTY_LEADER))

    fun getLeader() = cacheRole[RoleType.LEADER] ?: throw NoSuchElementException(messageError(RoleType.LEADER))

    fun getHigh() = cacheRole[RoleType.HIGH] ?: throw NoSuchElementException(messageError(RoleType.HIGH))

    override fun afterPropertiesSet() {
        initCache()
    }

    private fun initCache() {
        coDFamilyServer.getMyGuild().roles.forEach {
            val roleCod = RoleType.valueOf(it.name)
            cacheRole[roleCod] = it
        }
    }

    private fun messageError(param: RoleType): String = "Отсутствует роль %s в кеше.".format(param.nameInDiscord)
}