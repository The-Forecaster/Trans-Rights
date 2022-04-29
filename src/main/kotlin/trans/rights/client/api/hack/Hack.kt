package trans.rights.client.api.hack

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import trans.rights.TransRights.BasicEventManager
import trans.rights.TransRights.Companion.LOGGER
import trans.rights.client.api.Wrapper
import trans.rights.client.api.commons.Modular
import trans.rights.client.impl.setting.BooleanSetting
import trans.rights.client.impl.setting.EnumSetting
import trans.rights.client.impl.setting.NumberSetting
import trans.rights.client.impl.setting.Settings
import trans.rights.client.util.clearJson
import trans.rights.client.util.fromJson
import trans.rights.client.util.readString
import trans.rights.client.util.writeToJson
import java.io.File

abstract class Hack(
    name: String,
    description: String,
) : Modular(name, description), Wrapper {
    private var enabled: Boolean = false
    private val file: File = File("${HackManager.directory}/$name.json")
    val settings: Settings = Settings()

    init {
        if (!this.file.exists()) this.file.createNewFile()
    }

    protected fun enable() {
        if (!this.enabled) {
            BasicEventManager.register(this)

            this.onEnable()

            this.enabled = true
        }
    }

    protected fun disable() {
        if (this.enabled) {
            BasicEventManager.unregister(this)

            this.onDisable()

            this.enabled = false
        }
    }

    fun toggle() {
        if (this.enabled) disable() else enable()
    }

    open fun onEnable() {}

    open fun onDisable() {}

    fun load(file: File = this.file) {
        try {
            if (file.toPath().readString == "") {
                this.save(file)
                return
            }

            val json = file.toPath().fromJson(true)

            this.enabled = json.get("enabled").asBoolean

            settings.settings.forEach { setting ->
                when (setting) {
                    is BooleanSetting -> setting.set(json.get(setting.name).asBoolean)
                    is NumberSetting -> setting.set(json.get(setting.name).asDouble)
                    is EnumSetting -> setting.set(json.get(setting.name).asString)
                }
            }
        } catch (e: Exception) {
            file.toPath().clearJson()

            LOGGER.error("$name failed to load")

            e.printStackTrace()
        }
    }

    fun save(file: File = this.file) {
        try {
            val json = JsonObject()

            json.add("enabled", JsonPrimitive(enabled))

            for (setting in settings.settings) {
                when (setting) {
                    is BooleanSetting -> json.add(setting.name, JsonPrimitive(setting.value))
                    is NumberSetting -> json.add(setting.name, JsonPrimitive(setting.value))
                    is EnumSetting -> json.add(setting.name, JsonPrimitive(setting.value.toString()))
                }
            }

            file.toPath().writeToJson(json)
        } catch (e: Exception) {
            LOGGER.error("$name failed to save")

            e.printStackTrace()
        }
    }
}
