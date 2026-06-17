package cod.majestic.discord.bot.service

import cod.majestic.discord.bot.model.MainButton
import cod.majestic.discord.bot.model.enums.MainButtonType
import cod.majestic.discord.bot.model.enums.MainChannelType

interface MainButtonService {

    fun existsByButton(mainButtonType: MainButtonType): Boolean

    fun existsByButtons(mainButtonTypes: Collection<MainButtonType>): Boolean

    fun save(mainButton: MainButton): MainButton

    fun saveAll(mainButtons: Collection<MainButton>): Collection<MainButton>
}