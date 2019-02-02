package ru.ocelotjungle.bbyaplugin_gc.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.ocelotjungle.bbyaplugin_gc.CommandManager;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.*;
import static ru.ocelotjungle.bbyaplugin_gc.Main.scboard;
import static ru.ocelotjungle.bbyaplugin_gc.Main.server;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.*;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.argument;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentBuilder.literal;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.argumentPlayer;
import static ru.ocelotjungle.bbyaplugin_gc.commands.dirty.ArgumentEntityWrapper.getResultPlayer;

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
                    Player player = getResultPlayer(ctx, "target");
                    String originalName = player.getName();
                    String name = originalName.toLowerCase();

                    reloadGuildsCfg();
                    reloadPlayersCfg();

                    int playerInfo = fromHex(playersCfg.getString("players." + name)) & 0xFFFFFF,
                            newLevel = playerInfo & 0xFF,
                            guildId = (playerInfo >> 1*8) & 0xFF,
                            maxGuildLevel = guildsCfg.getInt(guildId + ".maxLevel");

                    if (newLevel >= maxGuildLevel) {
                        sender.sendMessage(format("Player %s already has max level of his guild (%d of %d).",
                                originalName, newLevel, maxGuildLevel));
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

                    int newScoreMoney = scboard.getObjective("Emerald_money").getScore(originalName).getScore(),
                            newScoreBottles = scboard.getObjective("ExpBottle").getScore(originalName).getScore(),
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

                    scboard.getObjective("Emerald_money").getScore(originalName).setScore(newScoreMoney);
                    scboard.getObjective("ExpBottle").getScore(originalName).setScore(newScoreBottles);
                    playersCfg.set("players." + name, toHex(playerInfo&0xFFFF00 | newLevel));

                    saveCfgs();
                    initCfgsToScoreboard(server.getPlayer(name), true);

                    return 0;
                }))
        );
    }

    @Override
    public String getDescription() {
        return "set guild level of a player to max";
    }
}
