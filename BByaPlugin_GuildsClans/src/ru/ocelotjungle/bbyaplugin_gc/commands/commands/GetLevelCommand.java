package ru.ocelotjungle.bbyaplugin_gc.commands.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.playersCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.format;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.fromHex;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandInterface;

public class GetLevelCommand implements CommandInterface {
	
	private static final int ARGUMENT_COUNT = 2;
	private static final String USAGE = "getlevel <player>",
								DESCRIPTION = "returns player's guild level";
	
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
		
		sender.sendMessage(format("%s's guild level is %s.",
			args[1], fromHex(playersCfg.getString("players." + name))&0xFF));
	}

}
