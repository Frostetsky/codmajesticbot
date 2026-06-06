package cod.majestic.discord.bot.service.impl

import cod.majestic.discord.bot.model.MainButton
import cod.majestic.discord.bot.model.enums.MainButtonType
import cod.majestic.discord.bot.repository.MainButtonRepository
import cod.majestic.discord.bot.service.MainButtonService
import org.springframework.stereotype.Service

@Service
class MainButtonServiceImpl(
    private val mainButtonRepository: MainButtonRepository
) : MainButtonService {

    override fun existsByButton(mainButtonType: MainButtonType): Boolean =
        mainButtonRepository.existsByNameButton(mainButtonType)

    override fun existsByButtons(mainButtonTypes: Collection<MainButtonType>): Boolean =
        mainButtonRepository.existsByNameButtonIn(mainButtonTypes)

    override fun save(mainButton: MainButton): MainButton = mainButtonRepository.save(mainButton)

    override fun saveAll(mainButtons: Collection<MainButton>): Collection<MainButton> = mainButtonRepository.saveAll(mainButtons)
}