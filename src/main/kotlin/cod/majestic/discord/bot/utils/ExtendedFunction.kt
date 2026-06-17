package cod.majestic.discord.bot.utils

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent

fun ModalInteractionEvent.getRequiredLongValue(id: String): Long =
    this.getValue(id)?.asString?.toLong() ?: throw NoSuchElementException("Long value not found by id %s.".format(id))