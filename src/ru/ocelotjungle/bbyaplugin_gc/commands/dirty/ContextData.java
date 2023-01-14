package ru.ocelotjungle.bbyaplugin_gc.commands.dirty;

import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R2.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;

public class ContextData {
    private final CommandSender sender;
    private final CommandListenerWrapper commandListenerWrapper;

    public ContextData(CommandSender sender) {
        this.sender = sender;
        if(sender instanceof CraftBlockCommandSender) {
            CraftBlockCommandSender senderCast = (CraftBlockCommandSender) sender;
            this.commandListenerWrapper = senderCast.getWrapper();
        } else if(sender instanceof CraftPlayer) {
            EntityPlayer senderCast = ((CraftPlayer) sender).getHandle();
            this.commandListenerWrapper = senderCast.cY();
        } else {
            this.commandListenerWrapper = MinecraftServer.getServer().aC();
        }
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public CommandListenerWrapper getCommandListenerWrapper() {
        return commandListenerWrapper;
    }
}
