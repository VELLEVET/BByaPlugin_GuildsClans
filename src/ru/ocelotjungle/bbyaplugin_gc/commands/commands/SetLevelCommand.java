package ru.ocelotjungle.bbyaplugin_gc.commands.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.ocelotjungle.bbyaplugin_gc.Logger;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandInterface;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.IncorrectValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.*;
import static ru.ocelotjungle.bbyaplugin_gc.Main.server;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.*;

public class SetLevelCommand implements CommandInterface {

	private static final int ARGUMENT_COUNT = 3;
	private static final String USAGE = "setlevel <player> <level>",
								DESCRIPTION = "sets player's guild level";

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
		List<String> result = new ArrayList<String>();
		
		args[2] = args[2].toLowerCase();
		
		for (Player player : server.getOnlinePlayers()) {
			String playerName = player.getName();
			
			if (playerName.toLowerCase().startsWith(args[2])) {
				result.add(playerName);
			}
		}
		
		return result;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		String name = args[1].toLowerCase(Locale.ENGLISH);
		
		int playerInfo = fromHex(playersCfg.getString("players." + name)) & 0xFFFFFF,
			maxLevel = guildsCfg.contains(((playerInfo>>1*8)&0xFF) + ".maxLevel")?
						guildsCfg.getInt(((playerInfo>>1*8)&0xFF) + ".maxLevel") : 255;
						
		try {
			short value = Short.parseShort("0" + args[2].replaceAll("[^0-9]", ""));
			
			if (value < 0 || value > maxLevel) {
				throw new NumberFormatException();
			}
		
			playersCfg.set("players." + name, toHex(playerInfo&0xFFFF00 | value));
			sender.sendMessage(format("You set guild level of player %s to %d.", args[1], value));
			
			saveCfgs();
			initCfgsToScoreboard(server.getPlayer(name), true);

		} catch (NumberFormatException nfe) {
			
			Logger.err(nfe.getMessage());
			throw new IncorrectValueException(format("You can set only values from 0 to %d.", maxLevel));
		}
	}
}
