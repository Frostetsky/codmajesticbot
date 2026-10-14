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
}