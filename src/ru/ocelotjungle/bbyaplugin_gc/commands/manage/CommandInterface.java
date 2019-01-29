package ru.ocelotjungle.bbyaplugin_gc.commands.manage;

/*******************************************
 *                                         *
 *        Main commands interface          *
 *                                         *
 *******************************************/

import org.bukkit.command.CommandSender;

import java.util.List;

public interface CommandInterface {
	
	public int getArgumentCount();
	
	String getUsage();
	String getDescription();
	
	List<String> getTabComplete(String[] args);
	
	void execute(CommandSender sender, String label, String[] args) throws 
			IncorrectValueException, IncorrectArgumentCountException, IllegalArgumentException, 
			NullPointerException;
}
