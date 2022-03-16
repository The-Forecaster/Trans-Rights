package trans.rights.client.modules.hack

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import java.io.File
import trans.rights.client.TransRights.Companion.LOGGER
import trans.rights.client.manager.impl.HackManager
import trans.rights.client.util.file.FileHelper
import trans.rights.client.misc.api.Globals
import trans.rights.client.modules.Module
import trans.rights.client.manager.impl.Settings
import trans.rights.client.modules.setting.settings.BooleanSetting
import trans.rights.client.modules.setting.settings.DoubleSetting
import trans.rights.client.modules.setting.settings.IntSetting
import trans.rights.event.bus.impl.BasicEventManager

abstract class Hack(
    name: String,
    description: String,
    val settings: Settings = Settings(),
    val file: File = File(HackManager.directory.absolutePath + "$name.json"),
    private var enabled: Boolean = false,
    val fileMang: FileHelper = FileHelper()
) : Module(name, description), Globals {

    private fun enable() {
        BasicEventManager.register(this)

        this.enabled = true
    }

    protected fun disable() {
        BasicEventManager.unregister(this)

        this.enabled = false
    }

    fun toggle() {
        if (this.enabled) disable() else enable()
    }

    open fun onEnable() {}

    open fun onDisable() {}

    fun load(file: File) {
        try {
            if (!file.exists())
                file.createNewFile()

            if (this.fileMang.read(this.file.toPath()) == "") {
                this.save(file)
                return
            }

            val json = this.fileMang.fromJson(file.toPath())

            for (setting in settings.values) {
                when (setting) {
                    is BooleanSetting -> setting.value = json.get(setting.name).asBoolean
                    is IntSetting -> setting.value = json.get(setting.name).asInt
                    is DoubleSetting -> setting.value = json.get(setting.name).asDouble
                }
            }
        } 
        catch (e: Exception) {
            this.fileMang.clearJson(file.toPath())

            LOGGER.error("$name failed to load")

            e.printStackTrace()
        }
    }

    fun save(file: File) {
        try {
            val json = JsonObject()

            for (setting in settings.values) {
                json.add(setting.name, JsonPrimitive(setting.value.toString()))
            }

            this.fileMang.writeToJson(json, file.toPath())
        } 
        catch (e: Exception) {
            LOGGER.error("$name failed to save")

            e.printStackTrace()
        }
    }
}
