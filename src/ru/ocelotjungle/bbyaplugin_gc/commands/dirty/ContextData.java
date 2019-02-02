package ru.ocelotjungle.bbyaplugin_gc.commands.dirty;

import net.minecraft.server.v1_13_R2.CommandListenerWrapper;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.MinecraftServer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_13_R2.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;

public class ContextData {
    private CommandSender sender;
    private CommandListenerWrapper commandListenerWrapper;

    public ContextData(CommandSender sender) {
        this.sender = sender;
        if(sender instanceof CraftBlockCommandSender) {
            CraftBlockCommandSender senderCast = (CraftBlockCommandSender) sender;
            this.commandListenerWrapper = senderCast.getWrapper();
        } else if(sender instanceof CraftPlayer) {
            EntityPlayer senderCast = ((CraftPlayer) sender).getHandle();
            this.commandListenerWrapper = senderCast.getCommandListener();
        } else {
            this.commandListenerWrapper = MinecraftServer.getServer().getServerCommandListener();
        }
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public CommandListenerWrapper getCommandListenerWrapper() {
        return commandListenerWrapper;
    }
}
