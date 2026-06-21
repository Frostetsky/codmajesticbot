package cod.majestic.discord.bot.listener.personal.branch

import cod.majestic.discord.bot.cache.DiscordRoleCache
import cod.majestic.discord.bot.component.ComponentId
import cod.majestic.discord.bot.component.FieldId
import cod.majestic.discord.bot.component.ModalId
import cod.majestic.discord.bot.model.PersonalBranch
import cod.majestic.discord.bot.server.CodFamilyServer
import cod.majestic.discord.bot.service.PersonalBranchService
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.modals.Modal
import org.springframework.stereotype.Component
import java.util.*

@Component
class CreatePersonalBranchListener(
    private val personalBranchService: PersonalBranchService,
    private val discordRoleCache: DiscordRoleCache,
    private val codFamilyServer: CodFamilyServer
) : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.componentId != ComponentId.CREATE_PERSONAL_BRANCH.getComponentId()) return

        val userId = event.user.idLong

        val channelExist = personalBranchService.existsByDiscordUserId(userId)

        if (channelExist) {
            return event.reply("У вас уже есть личная ветка.").setEphemeral(true).queue()
        }

        val modal = Modal.create(
            ModalId.CREATE_BRANCH.getModalId(),
            ModalId.CREATE_BRANCH.getTitle()
        )
            .addActionRows(
                ActionRow.of(
                    TextInput.create(FieldId.BRANCH_NAME.getId(), FieldId.BRANCH_NAME.getLabel(), FieldId.BRANCH_NAME.getSize())
                        .setRequired(FieldId.BRANCH_NAME.getRequired())
                        .setPlaceholder(FieldId.BRANCH_NAME.getPlaceHolder())
                        .build()
                )
            )
            .build()

        event.replyModal(modal).queue()
    }

    override fun onModalInteraction(event: ModalInteractionEvent) {
        if (event.modalId != ModalId.CREATE_BRANCH.getModalId()) return

        val member = event.member ?: return
        val guild = codFamilyServer.getMyGuild()

        val branchName = event.getValue(FieldId.BRANCH_NAME.getId())!!.asString.lowercase()

        val channelName = "\uD83C\uDF80・личная-ветка-${branchName}"
        val category = codFamilyServer.getMyGuild().categories.find { it.name == "\uD83D\uDD2F・Личные ветки" } ?: return

        val createdChannel = category.createTextChannel(channelName)
            .addPermissionOverride(
                guild.publicRole,
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                member,
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ADD_REACTION, Permission.CREATE_PUBLIC_THREADS, Permission.CREATE_PRIVATE_THREADS, Permission.MESSAGE_SEND_IN_THREADS, Permission.MESSAGE_ATTACH_FILES),
                null
            )
            .addPermissionOverride(
                discordRoleCache.getDepLeader(),
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                null
            )
            .addPermissionOverride(
                discordRoleCache.getLeader(),
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                null
            )
            .addPermissionOverride(
                discordRoleCache.getHigh(),
                listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ADD_REACTION, Permission.CREATE_PUBLIC_THREADS, Permission.CREATE_PRIVATE_THREADS, Permission.MESSAGE_SEND_IN_THREADS, Permission.MESSAGE_ATTACH_FILES),
                null
            )
            .addPermissionOverride(
                discordRoleCache.getRecruiter(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getMedia(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getTier3(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getTier2(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getTier1(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getWaitingCall(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getMain(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getTest(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getInactive(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getCod(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .addPermissionOverride(
                discordRoleCache.getP1(),
                null,
                listOf(Permission.VIEW_CHANNEL)
            )
            .complete()

        val personalBranch = PersonalBranch(
            UUID.randomUUID(),
            member.user.idLong,
            branchName,
            createdChannel.idLong
        )

        personalBranchService.save(personalBranch)

        event.reply("Личная ветка создана.")
            .setEphemeral(true)
            .queue()
    }
}