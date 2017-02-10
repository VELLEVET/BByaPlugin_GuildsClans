package ru.ocelotjungle.bbyaplugin_gc.commands.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.playersCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.guildsCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.format;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.fromHex;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandInterface;

public class GetGuildCommand implements CommandInterface {
	
	private static final int ARGUMENT_COUNT = 2;
	private static final String USAGE = "getguild <player>",
								DESCRIPTION = "returns player's guild";
	
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
		String name = args[1].toLowerCase();
		
		int guild = (fromHex(playersCfg.getString("players." + name))>>1*8) & 0xFF;
		sender.sendMessage(format("%s's guild is (%s; %d).",
			args[1], guildsCfg.getString(guild + ".engName"), guild));
	}
}
