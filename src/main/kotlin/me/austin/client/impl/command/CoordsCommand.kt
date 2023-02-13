package me.austin.client.impl.command

import com.mojang.brigadier.Command.SINGLE_SUCCESS
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import me.austin.client.api.command.Command
import me.austin.client.api.Wrapper

class CoordsCommand : Command("coords", "Copies your current coordinates to the clipboard", "/coords"), Wrapper {
    override fun build(builder: LiteralArgumentBuilder<FabricClientCommandSource>): LiteralArgumentBuilder<FabricClientCommandSource> = builder.executes({
        if (player != null) {
            val text = Math.floor(player!!.getX()).toString() + " " + Math.floor(player!!.getY()) + " " + Math.floor(player!!.getZ());
            minecraft.keyboard.setClipboard(text);
        }
        SINGLE_SUCCESS;
    })
}
