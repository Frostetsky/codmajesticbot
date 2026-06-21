package cod.majestic.discord.bot.listener.admin.strategy

import cod.majestic.discord.bot.cache.DiscordChannelCache
import cod.majestic.discord.bot.dto.AddPointToMemberRequest
import cod.majestic.discord.bot.model.EventCrime
import cod.majestic.discord.bot.model.enums.AdminCommand
import cod.majestic.discord.bot.model.enums.EventCrimeServer
import cod.majestic.discord.bot.model.enums.MainChannelType
import cod.majestic.discord.bot.service.EventCrimeService
import cod.majestic.discord.bot.service.FamilyMemberService
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.math.abs

@Component
class AddPointToMember(
    private val familyMemberService: FamilyMemberService,
    private val eventCrimeService: EventCrimeService,
    private val discordChannelCache: DiscordChannelCache
) : CommandHandler<AddPointToMemberRequest> {

    @Transactional
    override fun execute(body: AddPointToMemberRequest): Pair<TextChannel, String> {
        val userId = parseAsMentionToUserId(body.asMentionUser)
        val familyMember = familyMemberService.getByUserId(userId)
        familyMember.points += body.points

        familyMemberService.save(familyMember)

        val eventCrime = EventCrime(
            id = UUID.randomUUID(),
            type = body.typeRp,
            actionTime = body.actionTime,
            server = EventCrimeServer.SERVER_18,
            discordMessageId = abs(Random().nextLong()),
            familyMember = familyMember
        )

        eventCrimeService.save(eventCrime)

        return Pair(
            replyChannel(),
            replyMessage().format(
                body.asMentionUser,
                body.points,
                body.typeRp.getDescription(),
                body.whoUserActionAsMention
            )
        )
    }

    override fun command(): AdminCommand = AdminCommand.ADD_POINT_TO_MEMBER

    override fun replyChannel(): TextChannel = discordChannelCache.getBy(MainChannelType.ACTIVITY_FAMILY_MEMBER_HISTORY)

    override fun replyMessage(): String = "%s зачислено %s баллов за %s. Зачислил: %s"

}