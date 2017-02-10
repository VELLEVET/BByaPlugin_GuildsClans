package ru.ocelotjungle.bbyaplugin_gc.commands.commands;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.guildsCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.playersCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.saveCfgs;
import static ru.ocelotjungle.bbyaplugin_gc.Main.server;
import static ru.ocelotjungle.bbyaplugin_gc.Main.scboard;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.format;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.fromHex;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.initCfgsToScoreboard;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.rebuildPlayerNickname;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.toHex;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.Score;

import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandInterface;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.IncorrectValueException;

public class GoToGuildCommand implements CommandInterface {

	private static final int ARGUMENT_COUNT = 3;
	private static final String USAGE = "gotoguild <player> <guild ID/name>",
								DESCRIPTION = "moves player into guild and set his level to 1";
	
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
		List<String> result = new ArrayList<String>();
		
		args[2] = args[2].toLowerCase();
		
		for (String guildId : guildsCfg.getValues(false).keySet()) {
			String guildEngName = guildsCfg.getString(guildId + ".engName");
			
			if (guildEngName.toLowerCase().startsWith(args[2])) {
				result.add(guildEngName);
			}
		}
		
		return result;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		String name = args[1].toLowerCase();
		int playerInfo = fromHex(playersCfg.getString("players." + name)) & 0xFFFFFF;
		Score 	scoreMoney = scboard.getObjective("Emerald_money").getScore(args[1]),
				scoreBottles = scboard.getObjective("ExpBottle").getScore(args[1]);
		
		
		try {
			int guild = Integer.parseInt(args[2]);
			if (guild >= 1 && guild <= 255) {
				if (guild == 0 || guildsCfg.getValues(false).containsKey(guild + "")) {
					int costMoney = guildsCfg.getInt(guild + ".levelup.money"),
						costBottles = guildsCfg.getInt(guild + ".levelup.bottles");
					
					if (scoreMoney.getScore() >= costMoney && scoreBottles.getScore() >= costBottles) {
						if (playersCfg.contains("players." + name)) {
							playersCfg.set("players." + name, toHex(playerInfo&0xFF0000 | guild<<1*8 | 0x01));
							
						} else {
							playersCfg.set("players." + name, toHex(guild<<1*8 | 0x01));
						}
						
						scoreMoney.setScore(scoreMoney.getScore() - guildsCfg.getInt(guild + ".levelup.money"));
						scoreBottles.setScore(scoreBottles.getScore() - guildsCfg.getInt(guild + ".levelup.bottles"));
						
						sender.sendMessage(format("You moved player %s to guild (%s; %s).", 
								args[1], guildsCfg.getString(guild + ".engName"), guild));
					} else {
						sender.sendMessage(ChatColor.RED + format("Player %s has only %d of %d money and %d of %d bottles on his score.",
								args[1], scoreMoney.getScore(), costMoney, scoreBottles.getScore(), costBottles));
					}
					
				} else {
					sender.sendMessage(ChatColor.RED + "Guild with ID '" + guild + "' doesn't exist.");
				}
				
			} else {
				throw new IncorrectValueException(ChatColor.RED + "You can set only values from 1 to 255.");
			}
			
		} catch (NumberFormatException nfe) {
			String guildName = args[2];
			boolean found = false;
			
			for (String guild : guildsCfg.getValues(false).keySet()) {
				if (guildsCfg.getString(guild + ".engName").equalsIgnoreCase(guildName)) {
					int costMoney = guildsCfg.getInt(guild + ".levelup.money"),
						costBottles = guildsCfg.getInt(guild + ".levelup.bottles");
					
					if (scoreMoney.getScore() >= costMoney && scoreBottles.getScore() >= costBottles) {
						if (playersCfg.contains("players." + name)) {
							playersCfg.set("players." + name, toHex(playerInfo&0xFF0000 | Short.parseShort(guild)<<1*8 | 0x01));
							
						} else {
							playersCfg.set("players." + name, toHex(Short.parseShort(guild)<<1*8 | 0x01));
						}
						
						scoreMoney.setScore(scoreMoney.getScore() - guildsCfg.getInt(guild + ".levelup.money"));
						scoreBottles.setScore(scoreBottles.getScore() - guildsCfg.getInt(guild + ".levelup.bottles"));
						
						sender.sendMessage(format("You moved player %s to guild (%s; %s).", 
								args[1], guildsCfg.getString(guild + ".engName"), guild));
					} else {
						sender.sendMessage(ChatColor.RED + format("Player %s has only %d of %d money and %d of %d bottles on his score.",
								args[1], scoreMoney.getScore(), costMoney, scoreBottles.getScore(), costBottles));
					}
					found = true;
					break;
				}
			}
			if (!found) {
				sender.sendMessage(ChatColor.RED + "Guild with Name '" + guildName + "' doesn't exist.");
			}
		}
		
		saveCfgs();
		rebuildPlayerNickname(server.getPlayer(name));
		initCfgsToScoreboard();

	}

}
