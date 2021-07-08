package ru.ocelotjungle.bbyaplugin_gc.commands;

import org.bukkit.command.CommandSender;
import ru.ocelotjungle.bbyaplugin_gc.CommandManager;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.*;
import static ru.ocelotjungle.bbyaplugin_gc.Main.scboard;
import static ru.ocelotjungle.bbyaplugin_gc.Main.server;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.*;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.argument;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.literal;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.argumentPlayer;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.getPlayerName;

public class CommandFullLevelUp extends Command {
    public CommandFullLevelUp(CommandManager commandManager) {
        super(commandManager);
    }

    @Override
    protected void init() {
        mainNode = commandManager.register(
                literal("fulllevelup").then(argument("target", argumentPlayer(true))
                .executes((ctx) -> {
                    CommandSender sender = ctx.getSource().getSender();
                    String playerName = getPlayerName(ctx, "target");
                    String playerNameLowercase = playerName.toLowerCase();

                    reloadGuildsCfg();
                    reloadPlayersCfg();

                    int playerInfo = fromHex(playersCfg.getString("players." + playerNameLowercase)) & 0xFFFFFF,
                            newLevel = playerInfo & 0xFF,
                            guildId = (playerInfo >> 1*8) & 0xFF,
                            maxGuildLevel = guildsCfg.getInt(guildId + ".maxLevel");

                    if (newLevel >= maxGuildLevel) {
                        sender.sendMessage(format("Player %s already has max level of his guild (%d of %d).",
                                playerName, newLevel, maxGuildLevel));
                        return 0;
                    }

                    if (scboard.getObjective("Emerald_money") == null) {
                        scboard.registerNewObjective("Emerald_money", "dummy");
                    }

                    if (scboard.getObjective("ExpBottle") == null) {
                        scboard.registerNewObjective("ExpBottle", "dummy");
                    }

                    boolean doesStepAffectsMoney = mainCfg.getBoolean("stepAffectsMoney"),
                            doesStepAffectsBottles = mainCfg.getBoolean("stepAffectsBottles");

                    int newScoreMoney = scboard.getObjective("Emerald_money").getScore(playerName).getScore(),
                            newScoreBottles = scboard.getObjective("ExpBottle").getScore(playerName).getScore(),
                            costMoney = guildsCfg.getInt(guildId + ".levelup.money"),
                            costBottles = guildsCfg.getInt(guildId + ".levelup.bottles"),
                            costStep = guildsCfg.getInt(guildId + ".levelup.step");

                    sender.sendMessage("Start: level = " + newLevel +
                            ", emeralds = " + newScoreMoney +
                            ", bottles = " + newScoreBottles + ".");
                    sender.sendMessage("Needed: money = " + costMoney +
                            ", bottles = " + costBottles +
                            ", step = " + costStep);

                    while ((newScoreMoney >= costMoney + (doesStepAffectsMoney? newLevel*costStep : 0)) &&
                            (newScoreBottles >= costBottles + (doesStepAffectsBottles? newLevel*costStep : 0)) &&
                            (newLevel < maxGuildLevel)) {
                        newScoreMoney -= costMoney + (doesStepAffectsMoney? newLevel*costStep : 0);
                        newScoreBottles -= costBottles + (doesStepAffectsBottles? newLevel*costStep : 0);
                        newLevel++;
                    }

                    sender.sendMessage("End: level = " + newLevel +
                            ", emeralds = " + newScoreMoney +
                            ", bottles = " + newScoreBottles + ".");

                    scboard.getObjective("Emerald_money").getScore(playerName).setScore(newScoreMoney);
                    scboard.getObjective("ExpBottle").getScore(playerName).setScore(newScoreBottles);
                    playersCfg.set("players." + playerNameLowercase, toHex(playerInfo&0xFFFF00 | newLevel));

                    saveCfgs();
                    initCfgsToScoreboard(server.getPlayer(playerName), true);

                    return 0;
                }))
        );
    }

    @Override
    public String getDescription() {
        return "set guild level of a player to max";
    }
}
