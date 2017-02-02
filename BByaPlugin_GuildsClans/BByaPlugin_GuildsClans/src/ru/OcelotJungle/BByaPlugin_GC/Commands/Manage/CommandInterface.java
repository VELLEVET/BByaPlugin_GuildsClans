package ru.OcelotJungle.BByaPlugin_GC.Commands.Manage;

/*******************************************
 *                                         *
 *        Базовый интерфейс команд         *
 *                                         *
 *******************************************/

import java.util.List;

import org.bukkit.command.CommandSender;

public interface CommandInterface {
	
	int getArgumentCount();
	
	String getUsage();
	String getDescription();
	
	List<String> getTabComplete(String[] args);
	
	void execute(CommandSender sender, String label, String[] args) throws 
			IncorrectValueException, IncorrectArgumentCountException, IllegalArgumentException, 
			NullPointerException;
}
