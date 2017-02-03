package ru.ocelotjungle.bbyaplugin_gc.commands.commands;

import java.util.List;
import java.util.Locale;

import org.bukkit.command.CommandSender;

import ru.ocelotjungle.bbyaplugin_gc.Configs;
import ru.ocelotjungle.bbyaplugin_gc.Main;
import ru.ocelotjungle.bbyaplugin_gc.Utils;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandInterface;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.IncorrectValueException;

public class SetLevelCommand implements CommandInterface {

	private static final int argumentCount = 3;
	private static final String usage = "setlevel <player> <level>",
								description = "sets player's guild level";

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
		String name = args[1].toLowerCase(Locale.ENGLISH);
		
		try {
			if (Integer.parseInt(args[2]) >= 0 && Integer.parseInt(args[2]) <= 255) {
				Configs.playersCfg.set("players." + name, Utils.toHex(Utils
						.fromHex(Configs.playersCfg.getString("players." + name))& 0xFFFF00 | Integer.parseInt(args[2])));
				sender.sendMessage(Utils.format("You set guild level of player %s to %s.", args[1],args[2]));
				
				Configs.saveCfgs();
				Utils.rebuildPlayerNickname(Main.server.getPlayer(name));
				Utils.initCfgsToScoreboard();
	
			} else {
				throw new IncorrectValueException("You can set only values from 0 to 255.");
			}
		} catch (NumberFormatException nfe) {
			throw new IncorrectValueException("You can set only values from 0 to 255.");
		}
	}
}
