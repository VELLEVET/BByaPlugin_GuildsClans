package ru.ocelotjungle.bbyaplugin_gc.commands.manage;

/*******************************************
 *                                         *
 *      Processing of TAB pressing         *
 *                                         *
 *******************************************/

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import ru.ocelotjungle.bbyaplugin_gc.Main;

public class TabComplete implements TabCompleter {
	
	public TabComplete(Main plugin) {
		plugin.getCommand("bbyaplugingc").setTabCompleter(this);
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		try {
			if (args.length == 1) {
				List<String> result = new ArrayList<String>();
				
				for (String command : CommandManager.COMMANDS.keySet()) {
					if (command.startsWith(args[0])) {
						result.add(command);
					}
				}
				
				return result;
				
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("resetinfo")) {
					return CommandManager.COMMANDS.get(args[0]).getTabComplete(args);
				}
				
				List<String> result = new ArrayList<String>();
				
				for (Player player : Main.server.getOnlinePlayers()) {
					if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
						result.add(player.getName());
					}
				}
				
				return result;
				
			} else if (args.length == 3) {
				List<String> result = CommandManager.COMMANDS.get(args[0]).getTabComplete(args);
				return (result == null ? null : result);
			}
		} catch (NullPointerException npe) {
			return null;
		}
		
		return null;
	}

}
