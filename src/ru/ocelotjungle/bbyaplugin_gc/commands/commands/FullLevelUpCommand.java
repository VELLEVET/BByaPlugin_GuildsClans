package ru.ocelotjungle.bbyaplugin_gc.commands.commands;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.guildsCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.mainCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.playersCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.reloadGuildsCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.reloadPlayersCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.saveCfgs;
import static ru.ocelotjungle.bbyaplugin_gc.Main.scboard;
import static ru.ocelotjungle.bbyaplugin_gc.Main.server;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.format;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.fromHex;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.initCfgsToScoreboard;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.toHex;

import java.util.List;

import org.bukkit.command.CommandSender;

import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandInterface;

public class FullLevelUpCommand implements CommandInterface {
	
	private static final int ARGUMENT_COUNT = 2;
	private static final String USAGE = "fulllevelup <player>",
								DESCRIPTION = "fully levelups player in guild";
	
	@Override
	public int getArgumentCount() {
		return ARGUMENT_COUNT;
	}

	@Override
	public String getUsage() {
		return USAGE;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public List<String> getTabComplete(String[] args) {
		return null;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		String name = args[1].toLowerCase();
		
		reloadGuildsCfg();
		reloadPlayersCfg();

		int playerInfo = fromHex(playersCfg.getString("players." + name)) & 0xFFFFFF,
			newLevel = playerInfo & 0xFF,
			guildId = (playerInfo >> 1*8) & 0xFF,
			maxGuildLevel = guildsCfg.getInt(guildId + ".maxLevel");
		
		if (newLevel >= maxGuildLevel) {
			sender.sendMessage(format("Player %s already have max level of his guild (%d of %d).",
										args[1], newLevel, maxGuildLevel));
			return;
		}
		
		if (scboard.getObjective("Emerald_money") == null) {
			scboard.registerNewObjective("Emerald_money", "dummy");
		}
		
		if (scboard.getObjective("ExpBottle") == null) {
			scboard.registerNewObjective("ExpBottle", "dummy");
		}
		
		boolean doesStepAffectsMoney = mainCfg.getBoolean("stepAffectsMoney"),
				doesStepAffectsBottles = mainCfg.getBoolean("stepAffectsBottles");
		
		int newScoreMoney = scboard.getObjective("Emerald_money").getScore(args[1]).getScore(),
			newScoreBottles = scboard.getObjective("ExpBottle").getScore(args[1]).getScore(),
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
		
		scboard.getObjective("Emerald_money").getScore(args[1]).setScore(newScoreMoney);
		scboard.getObjective("ExpBottle").getScore(args[1]).setScore(newScoreBottles);
		playersCfg.set("players." + name, toHex(playerInfo&0xFFFF00 | newLevel));
		
		saveCfgs();
		initCfgsToScoreboard(server.getPlayer(name), true);
	}
}
