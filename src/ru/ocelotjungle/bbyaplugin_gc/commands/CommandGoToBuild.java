package ru.ocelotjungle.bbyaplugin_gc.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Score;
import ru.ocelotjungle.bbyaplugin_gc.CommandManager;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.*;
import static ru.ocelotjungle.bbyaplugin_gc.Main.scboard;
import static ru.ocelotjungle.bbyaplugin_gc.Main.server;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.*;
import static ru.ocelotjungle.bbyaplugin_gc.commands.arguments.GuildArgumentType.argumentGuild;
import static ru.ocelotjungle.bbyaplugin_gc.commands.arguments.GuildArgumentType.getGuild;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.argument;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.literal;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.argumentPlayer;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.getPlayerName;

public class CommandGoToBuild extends Command {
    public CommandGoToBuild(CommandManager commandManager) {
        super(commandManager);
    }

    @Override
    protected void init() {
        mainNode = commandManager.register(
                literal("gotoguild").then(argument("target", argumentPlayer(true))
                    .then(argument("guild", argumentGuild()).executes((ctx) -> {
                        CommandSender sender = ctx.getSource().getSender();
                        int guildId = getGuild(ctx, "guild");

                        if(guildId < 1) {
                            throw Exceptions.goToGuildOutOfRange.create();
                        }

                        String playerName = getPlayerName(ctx, "target");
                        String playerNameLowercase = playerName.toLowerCase();
                        int playerInfo = fromHex(playersCfg.getString("players." + playerNameLowercase)) & 0xFFFFFF;
                        Score scoreMoney = scboard.getObjective("Emerald_money").getScore(playerName);
                        Score scoreBottles = scboard.getObjective("ExpBottle").getScore(playerName);
                        int costMoney = guildsCfg.getInt(guildId + ".levelup.money");
                        int costBottles = guildsCfg.getInt(guildId + ".levelup.bottles");

                        if(scoreMoney.getScore() < costMoney) {
                            throw Exceptions.notEnoughMoney.create();
                        }

                        if(scoreBottles.getScore() < costBottles) {
                            throw Exceptions.notEnoughExperienceBottles.create();
                        }

                        if (playersCfg.contains("players." + playerNameLowercase)) {
                            playersCfg.set("players." + playerNameLowercase, toHex(playerInfo&0xFF0000 | guildId<<1*8 | 0x01));

                        } else {
                            playersCfg.set("players." + playerNameLowercase, toHex(guildId<<1*8 | 0x01));
                        }

                        scoreMoney.setScore(scoreMoney.getScore() - guildsCfg.getInt(guildId + ".levelup.money"));
                        scoreBottles.setScore(scoreBottles.getScore() - guildsCfg.getInt(guildId + ".levelup.bottles"));

                        saveCfgs();
                        initCfgsToScoreboard(server.getPlayer(playerName), true);

                        sender.sendMessage(format("You moved player %s to guild (%s; %s).",
                                playerName, guildsCfg.getString(guildId + ".engName"), guildId));

                        return 0;
                    })))
        );
    }

    @Override
    public String getDescription() {
        return "move player into guild and set his level to 1";
    }
}
