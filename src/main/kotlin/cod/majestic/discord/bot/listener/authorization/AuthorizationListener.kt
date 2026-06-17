package cod.majestic.discord.bot.listener.authorization

import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.model.FamilyMember
import cod.majestic.discord.bot.service.FamilyMemberService
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthorizationListener(
    private val familyMemberService: FamilyMemberService
) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.componentId != ComponentId.AUTHORIZATION.getComponentId()) return

        val member = event.member ?: return

        event.deferReply(true).complete()

        val userExist = familyMemberService.existsByDiscordUserId(member.user.idLong)

        if (userExist)  {

            event.hook.sendMessage("Вы уже зарегистрированы на сервере.")
                .setEphemeral(true)
                .complete()

        } else {

            val familyMember = FamilyMember(
                id = UUID.randomUUID(),
                discordProfileName = member.user.name,
                discordUsername = member.user.effectiveName,
                discordUserId = member.user.idLong,
                points = 0
            )

            val saved = familyMemberService.save(familyMember)

            event.hook.sendMessage("Зарегистрирован член семьи с уникальным идентификатором %s".format(saved.id))
                .setEphemeral(true)
                .complete()
        }
    }
}