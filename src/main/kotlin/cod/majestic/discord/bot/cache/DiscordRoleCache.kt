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

    fun getMedia() = cacheRole[RoleType.MEDIA] ?: throw NoSuchElementException(messageError(RoleType.MEDIA))

    fun getMain() = cacheRole[RoleType.MAIN] ?: throw NoSuchElementException(messageError(RoleType.MAIN))

    fun getTest() = cacheRole[RoleType.TEST] ?: throw NoSuchElementException(messageError(RoleType.TEST))

    fun getRecruiter() = cacheRole[RoleType.RECRUITER] ?: throw NoSuchElementException(messageError(RoleType.RECRUITER))

    fun getDepLeader() = cacheRole[RoleType.DEPUTY_LEADER] ?: throw NoSuchElementException(messageError(RoleType.DEPUTY_LEADER))

    fun getLeader() = cacheRole[RoleType.LEADER] ?: throw NoSuchElementException(messageError(RoleType.LEADER))

    fun getHigh() = cacheRole[RoleType.HIGH] ?: throw NoSuchElementException(messageError(RoleType.HIGH))

    fun getWaitingCall() = cacheRole[RoleType.WAITING_CALL] ?: throw NoSuchElementException(messageError(RoleType.WAITING_CALL))

    fun getTier1() = cacheRole[RoleType.TIER_1] ?: throw NoSuchElementException(messageError(RoleType.TIER_1))

    fun getTier2() = cacheRole[RoleType.TIER_2] ?: throw NoSuchElementException(messageError(RoleType.TIER_2))

    fun getTier3() = cacheRole[RoleType.TIER_3] ?: throw NoSuchElementException(messageError(RoleType.TIER_3))

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