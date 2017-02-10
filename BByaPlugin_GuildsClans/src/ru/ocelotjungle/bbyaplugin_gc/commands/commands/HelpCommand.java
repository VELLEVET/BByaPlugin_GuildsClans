package ru.ocelotjungle.bbyaplugin_gc.commands.commands;

import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import ru.ocelotjungle.bbyaplugin_gc.Logger;
import ru.ocelotjungle.bbyaplugin_gc.Main;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandInterface;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandManager;

@SuppressWarnings("unused")
public class HelpCommand implements CommandInterface {
	
	private static final int ARGUMENT_COUNT = 1;
	private static final String USAGE = "help",
								DESCRIPTION = "displays help";
	
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
		ChatColor ends = ChatColor.DARK_PURPLE, command = ChatColor.DARK_GREEN, desc = ChatColor.GREEN;
		String start = new String(command + "/" + label + " ");
		
		sender.sendMessage(ends + "<------------ Help for Guilds&Clans ------------>");
		for (Map.Entry<String, CommandInterface> entry : CommandManager.COMMANDS.entrySet()) {
			sender.sendMessage(start + entry.getKey() + " " + desc + "- " + entry.getValue().getDescription());
		}
		sender.sendMessage(ends + "<------------ Help for Guilds&Clans ------------>");
	}
}