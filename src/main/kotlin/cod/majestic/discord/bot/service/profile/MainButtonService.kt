package cod.majestic.discord.bot.service.profile

import cod.majestic.discord.bot.model.MainButton
import cod.majestic.discord.bot.model.enums.MainButtonType

interface MainButtonService {

    fun existsByButton(mainButtonType: MainButtonType): Boolean

    fun save(mainButton: MainButton): MainButton
}