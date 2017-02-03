package ru.ocelotjungle.bbyaplugin_gc.commands.manage;

/*******************************************
 *                                         *
 *        Main commands interface          *
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
