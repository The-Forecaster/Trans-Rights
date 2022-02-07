package trans.rights.client.modules

import trans.rights.client.modules.command.CommandManager
import trans.rights.client.modules.hack.HackManager

abstract class Manager<T : Module> : Module("", "") {
    companion object : Manager<Manager<*>>() {
        override fun load() {
            this.add(HackManager)
            this.add(CommandManager)
        }

        override fun unload() {
            this.values.clear()
        }
    }

    val values: MutableList<T> = mutableListOf()

    abstract fun load()

    fun add(value: T): T {
        this.values += value

        return value
    }

    open fun unload() {
        this.values.clear()
    }
}
