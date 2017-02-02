package ru.OcelotJungle.BByaPlugin_GC.Commands.Commands;

import java.util.List;
import java.util.Locale;

import org.bukkit.command.CommandSender;

import ru.OcelotJungle.BByaPlugin_GC.Configs;
import ru.OcelotJungle.BByaPlugin_GC.Main;
import ru.OcelotJungle.BByaPlugin_GC.SomeStuff;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Manage.CommandInterface;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Manage.IncorrectValueException;

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
				Configs.playersCfg.set("players." + name, SomeStuff.toHex(SomeStuff
						.fromHex(Configs.playersCfg.getString("players." + name))& 0xFFFF00 | Integer.parseInt(args[2])));
				sender.sendMessage(SomeStuff.format("You set guild level of player %s to %s.", args[1],args[2]));
				
				Configs.saveCfgs();
				SomeStuff.rebuildPlayerNickname(Main.server.getPlayer(name));
				SomeStuff.initCfgsToScoreboard();
	
			} else {
				throw new IncorrectValueException("You can set only values from 0 to 255.");
			}
		} catch (NumberFormatException nfe) {
			throw new IncorrectValueException("You can set only values from 0 to 255.");
		}
	}
}
