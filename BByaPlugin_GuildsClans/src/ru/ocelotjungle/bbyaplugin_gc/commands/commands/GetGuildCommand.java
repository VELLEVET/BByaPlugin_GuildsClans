package ru.ocelotjungle.bbyaplugin_gc.commands.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import ru.ocelotjungle.bbyaplugin_gc.Configs;
import ru.ocelotjungle.bbyaplugin_gc.Utils;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandInterface;

public class GetGuildCommand implements CommandInterface {
	
	private static final int argumentCount = 2;
	private static final String usage = "getguild <player>",
								description = "returns player's guild";
	
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
		
		int guildId = (Utils.fromHex(Configs.playersCfg.getString("players." + name))>>8)&0xFF;
		sender.sendMessage(Utils.format("%s's guild is (%s; %d).",
			args[1], Configs.guildsCfg.getString("guilds." + guildId + ".engName"), guildId));
	}
}
