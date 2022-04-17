package ru.ocelotjungle.bbyaplugin_gc.commands.dirty;

import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;

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
            this.commandListenerWrapper = senderCast.cQ();
        } else {
            this.commandListenerWrapper = MinecraftServer.getServer().aB();
        }
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public CommandListenerWrapper getCommandListenerWrapper() {
        return commandListenerWrapper;
    }
}
