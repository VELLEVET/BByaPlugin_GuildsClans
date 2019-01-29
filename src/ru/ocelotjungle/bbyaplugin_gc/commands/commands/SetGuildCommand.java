package ru.ocelotjungle.bbyaplugin_gc.commands.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandInterface;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.IncorrectValueException;

import java.util.ArrayList;
import java.util.List;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.*;
import static ru.ocelotjungle.bbyaplugin_gc.Main.server;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.*;

public class SetGuildCommand implements CommandInterface {
	
	private static final int ARGUMENT_COUNT = 3;
	private static final String USAGE = "setguild <player> <guild ID/name>",
								DESCRIPTION = "sets player's guild";
	
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
		
		try {
			int guild = Integer.parseInt(args[2]);
			if (guild >= 0 && guild <= 255) {
				if (guild == 0 || guildsCfg.getValues(false).containsKey(guild + "")) {
					if (playersCfg.contains("players." + name)) {
						playersCfg.set("players." + name, toHex(guild<<8 | playerInfo&0xFF00FF));
						
					} else {
						playersCfg.set("players." + name, toHex(guild<<8));
					}
					
					sender.sendMessage(format("You set guild (%s; %s) for player %s.", 
							guildsCfg.getString(args[2] + ".engName"), guild, args[1]));
					
				} else {
					sender.sendMessage(ChatColor.RED + "Guild with ID '" + guild + "' doesn't exist.");
				}
				
			} else {
				throw new IncorrectValueException(ChatColor.RED + "You can set only values from 0 to 255.");
			}
			
		} catch (NumberFormatException nfe) {
			String guildName = args[2];
			boolean found = false;
			for (String guild : guildsCfg.getValues(false).keySet()) {
				if (guildsCfg.getString(guild + ".engName").equalsIgnoreCase(guildName)) {
					if (playersCfg.contains("players." + name)) {
						playersCfg.set("players." + name, toHex((Byte.parseByte(guild)<<8 | playerInfo&0xFF00FF)));
						
					} else {
						playersCfg.set("players." + name, toHex(Byte.parseByte(guild)<<8));
					}
					
					sender.sendMessage(format("You set guild (%s; %s) for player %s.", 
							guildsCfg.getString(guild + ".engName"), guild, args[1]));
					found = true;
					break;
				}
			}
			if (!found) {
				sender.sendMessage(ChatColor.RED + "Guild with Name '" + guildName + "' doesn't exist.");
			}
		}
		
		saveCfgs();
		initCfgsToScoreboard(server.getPlayer(name), true);
	}
}
