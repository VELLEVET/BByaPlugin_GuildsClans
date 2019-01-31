package ru.ocelotjungle.bbyaplugin_gc.commands.utils;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.server.v1_13_R2.CommandDispatcher;
import net.minecraft.server.v1_13_R2.CommandListenerWrapper;
import net.minecraft.server.v1_13_R2.MinecraftServer;

public abstract class CommandDispatcherWrapper {
    public static LiteralArgumentBuilder<CommandListenerWrapper> literal(final String literal) {
        return CommandDispatcher.a(literal);
    }

    public static <T> RequiredArgumentBuilder<CommandListenerWrapper, T> required(final String name, ArgumentType<T> argument) {
        return CommandDispatcher.a(name, argument);
    }

    public static CommandListenerWrapper getCommandListener() {
        return MinecraftServer.getServer().getServerCommandListener();
    }
}
