package cod.majestic.discord.bot.listener.points

import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.service.FamilyMemberService
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class GetCountMyPointsListener(
    private val familyMemberService: FamilyMemberService
) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (!event.componentId.startsWith(ComponentId.GET_MY_POINTS.getComponentId())) return

        val targetMemberId = ComponentId.GET_MY_POINTS.getIdentifierParam(event.componentId)
        val pointsMemberFamily = familyMemberService.getPointsByUserId(targetMemberId)

        event.reply("Ваше текущее количество баллов ${pointsMemberFamily}.").queue()
    }
}