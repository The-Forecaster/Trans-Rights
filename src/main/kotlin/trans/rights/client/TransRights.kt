package trans.rights.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.impl.FabricLoaderImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import trans.rights.client.manager.Manager
import trans.rights.client.manager.impl.HackManager
import trans.rights.event.bus.impl.BasicEventManager

class TransRights : ClientModInitializer {
    companion object {
        const val NAME: String = "Trans-Rights"

        val mainDirectory = FabricLoaderImpl.INSTANCE.configDir

        @JvmField var LOGGER: Logger = LoggerFactory.getLogger(NAME)
    }

    init {
        BasicEventManager.register(this)
    }

    override fun onInitializeClient() {
        val start = System.currentTimeMillis()

        Manager.load()

        Runtime.getRuntime().addShutdownHook(Thread { HackManager.save() })

        LOGGER.info("Trans Rights has been started in " + (System.currentTimeMillis() - start) + " ms!")
    }
}
