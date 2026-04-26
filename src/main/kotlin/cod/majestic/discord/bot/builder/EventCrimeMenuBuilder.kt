package cod.majestic.discord.bot.builder

import cod.majestic.discord.bot.model.enums.EventCrimeType
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu

object EventCrimeMenuBuilder {

    fun get() =
        StringSelectMenu.create("")
            .addOption(EventCrimeType.MCL.name, EventCrimeType.MCL.description)
            .addOption(EventCrimeType.CAPT.name, EventCrimeType.CAPT.description)
            .addOption(EventCrimeType.VZM.name, EventCrimeType.VZM.description)
            .addOption(EventCrimeType.VZZ.name, EventCrimeType.VZZ.description)
            .addOption(EventCrimeType.GG.name, EventCrimeType.GG.description)
            .addOption(EventCrimeType.DILLERS.name, EventCrimeType.DILLERS.description)
            .addOption(EventCrimeType.CEHA.name, EventCrimeType.CEHA.description)
            .addOption(EventCrimeType.POSTAVKA.name, EventCrimeType.POSTAVKA.description)
            .addOption(EventCrimeType.DROP.name, EventCrimeType.DROP.description)
            .addOption(EventCrimeType.TAINIKI.name, EventCrimeType.TAINIKI.description)
            .addOption(EventCrimeType.STRELA.name, EventCrimeType.STRELA.description)
            .build()

}