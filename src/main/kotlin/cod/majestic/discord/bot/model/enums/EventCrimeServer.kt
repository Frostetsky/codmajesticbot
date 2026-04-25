package cod.majestic.discord.bot.model.enums

enum class EventCrimeServer(
    private val nameServer: String,
    private val number: Int
) {

    SERVER_2("Detroit (2)", 2),
    SERVER_6("San-Diego (6)", 6),
    SERVER_11("Dallas (11)", 11),
    SERVER_14("Seattle (14)", 14),
    SERVER_15("Phoenix (15)", 15),
    SERVER_16("Denver (16)", 16),
    SERVER_17("Portland (17)", 17),
    SERVER_18("Orlando (18)", 18),
    SERVER_MCL("MCL", 0);

    fun getNameServer() = nameServer
    fun getNumber() = number

    companion object {
        fun findByNumber(number: Int): EventCrimeServer {
            return entries.find { it.number == number } ?: throw IllegalStateException("Unsupported server number $number")
        }
    }

}