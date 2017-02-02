package ru.OcelotJungle.BByaPlugin_GC.Commands.Commands;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;

import ru.OcelotJungle.BByaPlugin_GC.Configs;
import ru.OcelotJungle.BByaPlugin_GC.Main;
import ru.OcelotJungle.BByaPlugin_GC.SomeStuff;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Manage.CommandInterface;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Manage.IncorrectValueException;

public class SetGuildCommand implements CommandInterface {
	
	private static final int argumentCount = 3;
	private static final String usage = "setguild <player> <guild ID/name>",
								description = "sets player's guild";
	
	@Override
	public int getArgumentCount() {
		return argumentCount;
	}
	
	@Override
	public String getUsage() {
		return usage;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public List<String> getTabComplete(String[] args) {
		List<String> result = new ArrayList<String>();
		
		for (String guildId : ((MemorySection) Configs.guildsCfg.get("guilds")).getValues(false).keySet()) {
			String guildEngName = Configs.guildsCfg.getString("guilds." + guildId + ".engName");
			
			if (guildEngName.startsWith(args[2])) {
				result.add(guildEngName);
			}
		}
		
		return result;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		FileConfiguration guildsCfg = Configs.guildsCfg, playersCfg = Configs.playersCfg;
		String name = args[1].toLowerCase(), value = args[2];
		
		try {
			if (Integer.parseInt(value) >= 0 && Integer.parseInt(value) <= 255) {
				if (Integer.parseInt(value) == 0 || ((MemorySection) guildsCfg.get("guilds")).getValues(false).containsKey(value)) {
					if (!playersCfg.contains("players." + name)) {
						playersCfg.set("players." + name, 
							SomeStuff.toHex(Integer.parseInt(value)<<8));
						
					} else {
						playersCfg.set("players." + name, 
							SomeStuff.toHex((Integer.parseInt(value)<<8 | SomeStuff.fromHex(playersCfg.getString("players." + name))&0xFF00FF)));
					}
					
					sender.sendMessage(SomeStuff.format("You set guild (%s; %s) for player %s.", 
							guildsCfg.getString("guilds." + args[2] + ".engName"), value, args[1]));
					
				} else {
					sender.sendMessage(ChatColor.RED + "Guild with ID/Name '" + value + "' doesn't exist.");
				}
				
			} else {
				throw new IncorrectValueException(ChatColor.RED + "You can set only values from 0 to 255.");
			}
			
		} catch (NumberFormatException nfe) {
			boolean found = false;
			for (String entry : ((MemorySection) guildsCfg.get("guilds")).getValues(false).keySet()) {
				if (guildsCfg.getString("guilds." + entry + ".engName").equalsIgnoreCase(value)) {
					if (!playersCfg.contains("players." + name)) {
						playersCfg.set("players." + name, 
							SomeStuff.toHex(Integer.parseInt(entry)<<8));
						
					} else {
						playersCfg.set("players." + name, 
							SomeStuff.toHex((Integer.parseInt(entry)<<8 | SomeStuff.fromHex(playersCfg.getString("players." + name))&0xFF00FF)));
					}
					
					sender.sendMessage(SomeStuff.format("You set guild (%s; %s) for player %s.", 
							guildsCfg.getString("guilds." + entry + ".engName"), entry, args[1]));
					found = true;
					break;
				}
			}
			if (!found) {
				sender.sendMessage(ChatColor.RED + "Guild with ID/Name '" + value + "' doesn't exist.");
			}
		}
		
		Configs.saveCfgs();
		SomeStuff.rebuildPlayerNickname(Main.server.getPlayer(name));
		SomeStuff.initCfgsToScoreboard();
	}
}
