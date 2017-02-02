package ru.OcelotJungle.BByaPlugin_GC.Commands.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import ru.OcelotJungle.BByaPlugin_GC.Configs;
import ru.OcelotJungle.BByaPlugin_GC.SomeStuff;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Manage.CommandInterface;

public class GetLevelCommand implements CommandInterface {
	
	private static final int argumentCount = 2;
	private static final String usage = "getlevel <player>",
								description = "returns player's guild level";
	
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
		return null;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		String name = args[1].toLowerCase();
		
		sender.sendMessage(SomeStuff.format("%s's guild level is %s.",
			args[1], SomeStuff.fromHex(Configs.playersCfg.getString("players." + name))&0xFF));
	}

}
