package ru.OcelotJungle.BByaPlugin_GC.Commands.Manage;

/*******************************************
 *                                         *
 *    Передача соответствующему классу     *
 *                                         *
 *******************************************/

import java.util.LinkedHashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;

import ru.OcelotJungle.BByaPlugin_GC.Main;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Commands.GetClanCommand;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Commands.GetGuildCommand;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Commands.GetLevelCommand;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Commands.HelpCommand;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Commands.ReloadCommand;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Commands.ResetInfoCommand;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Commands.SetClanCommand;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Commands.SetGuildCommand;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Commands.SetLevelCommand;

public class CommandManager implements CommandExecutor {
	
	public static final LinkedHashMap<String, CommandInterface> commands = new LinkedHashMap<String, CommandInterface>();
	ChatColor err = ChatColor.RED;
	
	public CommandManager(Main plugin) {
		plugin.getCommand("bbyaplugingc").setExecutor(this);
		
		commands.put("help", new HelpCommand());
		commands.put("reload", new ReloadCommand());
		commands.put("setclan", new SetClanCommand());
		commands.put("getclan", new GetClanCommand());
		commands.put("setguild", new SetGuildCommand());
		commands.put("getguild", new GetGuildCommand());
		commands.put("setlevel", new SetLevelCommand());
		commands.put("getlevel", new GetLevelCommand());
		commands.put("resetinfo", new ResetInfoCommand());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0 || !commands.containsKey(args[0])) {
			commands.get("help").execute(sender, label, args);
			return true;
			
		} else if (commands.containsKey(args[0])) {
			
			CommandInterface command = commands.get(args[0]);
			
			if (args.length < command.getArgumentCount()) {
				sender.sendMessage(err + "Usage: /bp " + command.getUsage());
				return true;
			}
			
			if (args.length == 1) {
				command.execute(sender, label, args);
				return true;
				
			} else if (args.length >= 2) {
				
				if (args[1].contains("@") && !(sender instanceof CraftBlockCommandSender)) {
					sender.sendMessage(err + "You can use identifiers only from Command Block.");
					return true;

				} else if ((args[1].equalsIgnoreCase("?") || args[1].contains(".")) && !(sender instanceof CraftPlayer)) {
					sender.sendMessage(err + "You can use '?' and '.' only as player.");
					return true;
					
				} else if ((args[1].equalsIgnoreCase("?") || args[1].contains(".")) && (sender instanceof CraftPlayer)) {
					args[1] = sender.getName();	
				}
				
				try {
					command.execute(sender, label, args);
					return true;
					
				} catch (IncorrectValueException ive) {
					sender.sendMessage(err + ive.getMessage());
					
				} catch (IncorrectArgumentCountException iace) {
					sender.sendMessage(err + "Usage: /bp " + command.getUsage() + " - " + command.getDescription());
					
				} catch (IllegalArgumentException iae) {
					sender.sendMessage(err + "Some IllegalArgumentException, please contact OcelotJungle!");
					//iae.printStackTrace();
					
				} catch (NullPointerException npe) {
					sender.sendMessage(err + "Some NullPointerException, please contact OcelotJungle!");
					//npe.printStackTrace();
					
				} catch (Exception e) {
					sender.sendMessage(err + "Some exception (" + e.getMessage() + "), please contact OcelotJungle!");
					//e.printStackTrace();
				}
			}
		}
		
		return true;
	}
}