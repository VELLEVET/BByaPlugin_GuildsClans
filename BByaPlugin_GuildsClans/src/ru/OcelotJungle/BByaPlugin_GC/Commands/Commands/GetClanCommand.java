package ru.OcelotJungle.BByaPlugin_GC.Commands.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import ru.OcelotJungle.BByaPlugin_GC.Configs;
import ru.OcelotJungle.BByaPlugin_GC.SomeStuff;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Manage.CommandInterface;

public class GetClanCommand implements CommandInterface {

	private static final int argumentCount = 2;
	private static final String usage = "getclan <player>",
								description = "returns player's clan";
	
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
		
		int clanId = (SomeStuff.fromHex(Configs.playersCfg.getString("players." + name))>>2*8)&0xFF;
		sender.sendMessage(SomeStuff.format("%s's clan is (%s; %d).",
			args[1], Configs.clansCfg.getString("clans." + clanId + ".label"), clanId));
	}
}
