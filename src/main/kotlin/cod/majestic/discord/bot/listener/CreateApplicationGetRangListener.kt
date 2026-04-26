package cod.majestic.discord.bot.listener

import cod.majestic.discord.bot.model.enums.RoleType as RoleCod
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.springframework.stereotype.Component

@Component
class CreateApplicationGetRangListener : ListenerAdapter() {

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (event.componentId == "create_application_get_rang") {

            val guild = event.guild ?: return
            val member = event.member ?: return

            val categoryGetRang = guild.categories.firstOrNull { it.name == "\uD83D\uDD2B・Тиры"}!!
            val existingChannel = guild.textChannels
                .firstOrNull { it.name == "\uD83D\uDEB9・заявка-на-получение-ранга-${member.id}" }

            val sanitizeUserName = sanitizeUsername(member.user.name)

            val personalBranchChannel = guild.textChannels.find { extractUsernameFromChannel(it.name) == sanitizeUserName }

            if (existingChannel != null) {
                event.reply("❌ У вас уже есть активная заявка: ${existingChannel.asMention}")
                    .setEphemeral(true)
                    .queue()
                return
            }

            if (personalBranchChannel == null) {
                event.reply("❌ У вас нет личной ветки.")
                    .setEphemeral(true)
                    .queue()
                return
            }

            val isMainPlus = member.roles.any { it.name == RoleCod.MAIN_PLUS.nameInDiscord }
            if (isMainPlus) {
                event.reply("У тебя максимально возможный ранг %s".format(RoleCod.MAIN_PLUS.nameInDiscord))
                    .setEphemeral(true)
                    .queue()
            }

            val deputyRole = guild.roles.find { it.name == RoleCod.DEPUTY_LEADER.nameInDiscord } ?: return
            val leaderRole = guild.roles.find { it.name == RoleCod.LEADER.nameInDiscord } ?: return
            val highRole = guild.roles.find { it.name == RoleCod.HIGH.nameInDiscord } ?: return

            val channelName = "\uD83D\uDEB9・заявка-на-получение-ранга-${member.id}"

            categoryGetRang.createTextChannel(channelName)
                .addPermissionOverride(
                    guild.publicRole,
                    null,
                    listOf(Permission.VIEW_CHANNEL)
                )
                .addPermissionOverride(
                    member,
                    listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                    null
                )
                .addPermissionOverride(
                    deputyRole,
                    listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                    null
                )
                .addPermissionOverride(
                    leaderRole,
                    listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                    null
                )
                .addPermissionOverride(
                    highRole,
                    listOf(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                    null
                )
                .queue { createdChannel ->

                    val message =
                        """
                    📋 **Заявка на получение ранга**

                    Запрос от пользователя: ${member.asMention}.
                    Личная ветка пользователя: ${personalBranchChannel.asMention}
                    Просьба пересмотреть мой текущий ранг и выдать ${getMessageByPriorityRole(member.roles)}.
                    Ниже прикрепляю все необходимые доказательства.
                    
                    """.trimIndent()

                    createdChannel.sendMessage(
                        message
                    ).setActionRow(
                        Button.success("approve_main_get_rang_${member.id}", "Выдать MAIN"),
                        Button.danger("approve_main_plus_get_rang_${member.id}", "Выдать MAIN PLUS"),
                        Button.primary("approve_cod_get_rang_${member.id}", "Выдать COD")
                    ).queue()

                    event.reply("Заявка создана: ${createdChannel.asMention}")
                        .setEphemeral(true)
                        .queue()
                }
        }
    }

    private fun getMessageByPriorityRole(roles: List<Role>): String {
        val isRp = roles.any { it.name == RoleCod.RP.nameInDiscord }
        val isCod = roles.any { it.name == RoleCod.COD.nameInDiscord }
        val isMain = roles.any { it.name == RoleCod.MAIN.nameInDiscord }

        if (isRp) {
            return RoleCod.COD.nameInDiscord
        }

        if (isCod) {
            return RoleCod.MAIN.nameInDiscord
        }

        if (isMain) {
            return RoleCod.MAIN_PLUS.nameInDiscord
        }

        throw UnsupportedOperationException("Неизвестно какой ранг запрашивает участник семьи. Произошёл конфликт ролей.")
    }

    /**
     * Удаляет все запрещенные символы из username для использования в названии текстового канала
     * Разрешены: a-z, A-Z, 0-9, _
     * Все остальные символы (включая . , - пробелы и т.д.) удаляются
     */
    private fun sanitizeUsername(username: String): String = username.replace("[^a-zA-Z0-9_]".toRegex(), "")

    /**
     * Извлекает username из имени канала формата "личная-ветка-{username}"
     *
     * @param channelName Имя канала (например, "личная-ветка-userofrq")
     * @return Извлеченный username или null, если формат не совпадает
     */
    private fun extractUsernameFromChannel(channelName: String): String? {

        val prefix = "\uD83D\uDEB9・личная-ветка-"

        if (channelName.startsWith(prefix)) {
            return channelName.substring(prefix.length)
        }

        return null;
    }
}