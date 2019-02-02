package ru.ocelotjungle.bbyaplugin_gc.commands;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public abstract class Exceptions {
    public static final SimpleCommandExceptionType guildNotFound = new SimpleCommandExceptionType(
            new LiteralMessage("Guild not found")
    );

    public static final SimpleCommandExceptionType goToGuildOutOfRange = new SimpleCommandExceptionType(
            new LiteralMessage("You can only go to guilds with IDs 1..255")
    );

    public static final SimpleCommandExceptionType notEnoughMoney = new SimpleCommandExceptionType(
            new LiteralMessage("The player has not enough money")
    );

    public static final SimpleCommandExceptionType notEnoughExperienceBottles = new SimpleCommandExceptionType(
            new LiteralMessage("The player has not enough experience bottles")
    );

    public static final SimpleCommandExceptionType wrongGuildLevel = new SimpleCommandExceptionType(
            new LiteralMessage("The guild level is too high")
    );

    public static final SimpleCommandExceptionType clanNotFound = new SimpleCommandExceptionType(
            new LiteralMessage("Clan not found")
    );

    public static final SimpleCommandExceptionType noPlayersFound = new SimpleCommandExceptionType(
            new LiteralMessage("No players found")
    );

    public static final SimpleCommandExceptionType tooManyPlayersFound = new SimpleCommandExceptionType(
            new LiteralMessage("Too many players found, expected only one")
    );
}
