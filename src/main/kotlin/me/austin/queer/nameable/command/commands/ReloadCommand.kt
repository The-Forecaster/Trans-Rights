package me.austin.queer.nameable.command.commands

import com.mojang.brigadier.CommandDispatcher
import me.austin.queer.Globals.*
import me.austin.queer.event.events.SystemEvent
import me.austin.queer.nameable.command.Command
import net.minecraft.server.command.CommandManager.*
import net.minecraft.server.command.ServerCommandSource

object ReloadCommand : Command("Reload") {
    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(literal(this.name).executes({reload()}))
    }

    private fun reload(): Int {
        EVENTBUS.post(SystemEvent.Save())

        return 0
    }
}
