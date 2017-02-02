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

public class SetClanCommand implements CommandInterface {
	
	private static final int argumentCount = 3;
	private static final String usage = "setclan <player> <clan ID/name>",
								description = "sets player's clan";
	
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
		
		for (String clanId : ((MemorySection) Configs.clansCfg.get("clans")).getValues(false).keySet()) {
			String clanLabel = Configs.clansCfg.getString("clans." + clanId + ".label");
			
			if (clanLabel.startsWith(args[2])) {
				result.add(clanLabel);
			}
		}
		
		return result;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {			
		FileConfiguration clansCfg = Configs.clansCfg, playersCfg = Configs.playersCfg;
		String name = args[1].toLowerCase(), value = args[2];
		
		try {
			if (Integer.parseInt(value) >= 0 && Integer.parseInt(value) <= 255) {
				if (Integer.parseInt(value) == 0 || ((MemorySection) Configs.clansCfg.get("clans")).getValues(false).containsKey(value)) {
					
					if (!playersCfg.contains("players." + name)) {
						playersCfg.set("players." + name, 
							SomeStuff.toHex(Integer.parseInt(value)<<2*8));	
					} else {
						playersCfg.set("players." + name, 
							SomeStuff.toHex((Integer.parseInt(value)<<2*8 | SomeStuff.fromHex(playersCfg.getString("players." + name))&0xFFFF)));
					}
					sender.sendMessage(SomeStuff.format("You set clan (%s; %s) for player %s.", 
							clansCfg.getString("clans." + value + ".label"), value, args[1]));
					
				} else {
					sender.sendMessage(ChatColor.RED + "Clan with ID/Name '" + value + "' doesn't exist.");
				}
				
			} else {
				throw new IncorrectValueException(ChatColor.RED + "You can set only values from 0 to 255.");
			}
			
		} catch (NumberFormatException nfe) {
			boolean found = false;
			for (String entry : ((MemorySection) clansCfg.get("clans")).getValues(false).keySet()) {
				if (clansCfg.getString("clans." + entry + ".label").equalsIgnoreCase(value)) {
					
					if (!playersCfg.contains("players." + name)) {
						playersCfg.set("players." + name, 
							SomeStuff.toHex(Integer.parseInt(entry)<<2*8));
					} else {
						playersCfg.set("players." + name, 
							SomeStuff.toHex((Integer.parseInt(entry)<<2*8 | SomeStuff.fromHex(playersCfg.getString("players." + name))&0xFFFF)));
					}
					sender.sendMessage(SomeStuff.format("You set clan (%s; %s) for player %s.", 
							clansCfg.getString("clans." + entry + ".label"), entry, args[1]));
					
					found = true;
					break;
				}
			}
			if (!found) {
				sender.sendMessage(ChatColor.RED + "Clan with ID/Name '" + value + "' doesn't exist.");
			}
		}
		
		Configs.saveCfgs();
		SomeStuff.rebuildPlayerNickname(Main.server.getPlayer(name));
		SomeStuff.initCfgsToScoreboard();
	}
}