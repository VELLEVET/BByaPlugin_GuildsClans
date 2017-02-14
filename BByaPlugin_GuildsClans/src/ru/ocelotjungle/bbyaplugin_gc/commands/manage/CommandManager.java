package ru.ocelotjungle.bbyaplugin_gc.commands.manage;

/*******************************************
 *                                         *
 *       Processing of typed command       *
 *                                         *
 *******************************************/

import java.util.LinkedHashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;

import ru.ocelotjungle.bbyaplugin_gc.Main;
import ru.ocelotjungle.bbyaplugin_gc.commands.commands.FullLevelUpCommand;
import ru.ocelotjungle.bbyaplugin_gc.commands.commands.GetClanCommand;
import ru.ocelotjungle.bbyaplugin_gc.commands.commands.GetGuildCommand;
import ru.ocelotjungle.bbyaplugin_gc.commands.commands.GetLevelCommand;
import ru.ocelotjungle.bbyaplugin_gc.commands.commands.GoToGuildCommand;
import ru.ocelotjungle.bbyaplugin_gc.commands.commands.HelpCommand;
import ru.ocelotjungle.bbyaplugin_gc.commands.commands.ReloadCommand;
import ru.ocelotjungle.bbyaplugin_gc.commands.commands.ResetInfoCommand;
import ru.ocelotjungle.bbyaplugin_gc.commands.commands.SetClanCommand;
import ru.ocelotjungle.bbyaplugin_gc.commands.commands.SetGuildCommand;
import ru.ocelotjungle.bbyaplugin_gc.commands.commands.SetLevelCommand;

public class CommandManager implements CommandExecutor {
	
	public static final LinkedHashMap<String, CommandInterface> COMMANDS = new LinkedHashMap<String, CommandInterface>();
	ChatColor err = ChatColor.RED;
	
	public CommandManager(Main plugin) {
		plugin.getCommand("bbyaplugingc").setExecutor(this);
		
		COMMANDS.put("help", new HelpCommand());
		COMMANDS.put("reload", new ReloadCommand());
		COMMANDS.put("setclan", new SetClanCommand());
		COMMANDS.put("getclan", new GetClanCommand());
		COMMANDS.put("setguild", new SetGuildCommand());
		COMMANDS.put("getguild", new GetGuildCommand());
		COMMANDS.put("setlevel", new SetLevelCommand());
		COMMANDS.put("getlevel", new GetLevelCommand());
		COMMANDS.put("gotoguild", new GoToGuildCommand());
		COMMANDS.put("fulllevelup", new FullLevelUpCommand());
		COMMANDS.put("resetinfo", new ResetInfoCommand());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0 || !COMMANDS.containsKey(args[0])) {
			COMMANDS.get("help").execute(sender, label, args);
			return true;
			
		} else if (COMMANDS.containsKey(args[0])) {
			
			CommandInterface command = COMMANDS.get(args[0]);
			
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