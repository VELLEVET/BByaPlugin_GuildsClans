package ru.ocelotjungle.bbyaplugin_gc.commands;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public abstract class Exceptions {
    public static final SimpleCommandExceptionType guildNotFound = new SimpleCommandExceptionType(
            new LiteralMessage("Guild not found")
    );

    public static final SimpleCommandExceptionType noPlayersFound = new SimpleCommandExceptionType(
            new LiteralMessage("No players found")
    );
}
