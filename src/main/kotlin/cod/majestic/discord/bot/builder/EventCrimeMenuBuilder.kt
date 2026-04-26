package cod.majestic.discord.bot.builder

import cod.majestic.discord.bot.model.enums.EventCrimeType
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu

object EventCrimeMenuBuilder {

    fun get() =
        StringSelectMenu.create("crime_type_select")
            .setPlaceholder("Выберите тип события")
            .addOption(EventCrimeType.MCL.description, EventCrimeType.MCL.name)
            .addOption(EventCrimeType.CAPT.description, EventCrimeType.CAPT.name)
            .addOption(EventCrimeType.VZM.description, EventCrimeType.VZM.name)
            .addOption(EventCrimeType.VZZ.description, EventCrimeType.VZZ.name)
            .addOption(EventCrimeType.GG.description, EventCrimeType.GG.name)
            .addOption(EventCrimeType.DILLERS.description, EventCrimeType.DILLERS.name)
            .addOption(EventCrimeType.CEHA.description, EventCrimeType.CEHA.name)
            .addOption(EventCrimeType.POSTAVKA.description, EventCrimeType.POSTAVKA.name)
            .addOption(EventCrimeType.DROP.description, EventCrimeType.DROP.name)
            .addOption(EventCrimeType.TAINIKI.description, EventCrimeType.TAINIKI.name)
            .addOption(EventCrimeType.STRELA.description, EventCrimeType.STRELA.name)
            .build()

}