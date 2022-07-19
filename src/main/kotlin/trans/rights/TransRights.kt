package trans.rights

import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import trans.rights.client.api.Wrapper
import trans.rights.client.api.command.CommandManager
import trans.rights.client.api.commons.Manager
import trans.rights.client.api.hack.HackManager
import trans.rights.client.impl.friend.FriendManager
import trans.rights.event.bus.EventManager
import java.nio.file.Files
import java.nio.file.Path

class TransRights : ModInitializer {
    companion object : Wrapper {
        const val NAME: String = "Trans-Rights"

        val mainDirectory: Path = Path.of("${minecraft.runDirectory}/${NAME.lowercase()}")

        @JvmStatic
        var LOGGER: Logger = LoggerFactory.getLogger(NAME)

        private val managers = listOf(FriendManager, HackManager, CommandManager)

        fun load() = managers.stream().forEach(Manager<*, *>::load)

        fun unload() = managers.stream().forEach(Manager<*, *>::unload)
    }

    init {
        if (!Files.exists(mainDirectory)) Files.createDirectory(mainDirectory)
    }

    override fun onInitialize() {
        val start = System.currentTimeMillis()

        LOGGER.info("Starting $NAME...")

        load()

        Runtime.getRuntime().addShutdownHook(Thread(::unload))

        LOGGER.info("$NAME has been started in ${System.currentTimeMillis() - start} ms!")
    }
}

object BasicEventManager : EventManager()
