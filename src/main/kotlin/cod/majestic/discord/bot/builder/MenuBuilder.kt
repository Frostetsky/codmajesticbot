package cod.majestic.discord.bot.builder

import cod.majestic.discord.bot.model.enums.EventCrimeType
import cod.majestic.discord.bot.model.enums.RoleType
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu

object MenuBuilder {

    fun getEventCrime() =
        StringSelectMenu.create("crime_type_select")
            .setPlaceholder("Выберите тип события")
            .addOption(EventCrimeType.GG.getDescription(), EventCrimeType.GG.name)
            .addOption(EventCrimeType.DILLERS.getDescription(), EventCrimeType.DILLERS.name)
            .addOption(EventCrimeType.CEHA.getDescription(), EventCrimeType.CEHA.name)
            .addOption(EventCrimeType.POSTAVKA.getDescription(), EventCrimeType.POSTAVKA.name)
            .addOption(EventCrimeType.FZ.getDescription(), EventCrimeType.FZ.name)
            .addOption(EventCrimeType.OSTROV.getDescription(), EventCrimeType.OSTROV.name)
            .addOption(EventCrimeType.DROP.getDescription(), EventCrimeType.DROP.name)
            .addOption(EventCrimeType.TAINIKI.getDescription(), EventCrimeType.TAINIKI.name)
            .build()

    fun getOtherGames() =
        StringSelectMenu.create("other_games_select_id")
            .setPlaceholder("Выберите дополнительную роль")
            .addOption(RoleType.CS.nameInDiscord, RoleType.CS.name)
            .addOption(RoleType.DOTA.nameInDiscord, RoleType.DOTA.name)
            .addOption(RoleType.PUBG.nameInDiscord, RoleType.PUBG.name)
            .addOption(RoleType.TANKI.nameInDiscord, RoleType.TANKI.name)
            .addOption(RoleType.MINECRAFT.nameInDiscord, RoleType.MINECRAFT.name)
            .addOption(RoleType.ROBLOX.nameInDiscord, RoleType.ROBLOX.name)
            .build()
}