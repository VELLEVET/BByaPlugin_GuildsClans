package ru.ocelotjungle.bbyaplugin_gc.commands.commands;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.clansCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.playersCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.saveCfgs;
import static ru.ocelotjungle.bbyaplugin_gc.Main.server;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.format;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.fromHex;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.initCfgsToScoreboard;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.toHex;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandInterface;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.IncorrectValueException;

public class SetClanCommand implements CommandInterface {
	
	private static final int ARGUMENT_COUNT = 3;
	private static final String USAGE = "setclan <player> <clan ID/name>",
								DESCRIPTION = "sets player's clan";
	
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
		
		for (String clanId : clansCfg.getValues(false).keySet()) {
			String clanLabel = clansCfg.getString(clanId + ".label");
			
			if (clanLabel.toLowerCase().startsWith(args[2])) {
				result.add(clanLabel);
			}
		}
		
		return result;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		String name = args[1].toLowerCase();
		int playerInfo = fromHex(playersCfg.getString("players." + name)) & 0xFFFFFF;
		
		try {
			int clan = Integer.parseInt(args[2]);
			if (clan >= 0 && clan <= 255) {
				if (clan == 0 || clansCfg.getValues(false).containsKey(clan + "")) {
					
					if (playersCfg.contains("players." + name)) {
						playersCfg.set("players." + name, toHex(clan<<2*8 | playerInfo&0xFFFF));
						
					} else {
						playersCfg.set("players." + name, toHex(clan<<2*8));	
					}
					sender.sendMessage(format("You set clan (%s; %s) for player %s.", 
							clansCfg.getString(clan + ".label"), clan, args[1]));
					
				} else {
					sender.sendMessage(ChatColor.RED + "Clan with ID '" + clan + "' doesn't exist.");
				}
				
			} else {
				throw new IncorrectValueException(ChatColor.RED + "You can set only values from 0 to 255.");
			}
			
		} catch (NumberFormatException nfe) {
			String clanName = args[2];
			boolean found = false;
			for (String clan : clansCfg.getValues(false).keySet()) {
				if (clansCfg.getString(clan + ".label").equalsIgnoreCase(clanName)) {
					
					if (playersCfg.contains("players." + name)) {
						playersCfg.set("players." + name, toHex((Byte.parseByte(clan)<<2*8 | playerInfo&0xFFFF)));
						
					} else {
						playersCfg.set("players." + name, toHex(Byte.parseByte(clan)<<2*8));
					}
					sender.sendMessage(format("You set clan (%s; %s) for player %s.", 
							clansCfg.getString(clan + ".label"), clan, args[1]));
					
					found = true;
					break;
				}
			}
			if (!found) {
				sender.sendMessage(ChatColor.RED + "Clan with Name '" + clanName + "' doesn't exist.");
			}
		}
		
		saveCfgs();
		initCfgsToScoreboard(server.getPlayer(name), true);
	}
}