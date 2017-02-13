package ru.ocelotjungle.bbyaplugin_gc.commands.commands;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.guildsCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.playersCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.saveCfgs;
import static ru.ocelotjungle.bbyaplugin_gc.Main.scboard;
import static ru.ocelotjungle.bbyaplugin_gc.Main.server;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.format;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.fromHex;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.initCfgsToScoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;

import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandInterface;

public class ResetInfoCommand implements CommandInterface {
	
	private static final int ARGUMENT_COUNT = 2;
	private static final String USAGE = "resetinfo <player>",
								DESCRIPTION = "resets player's info";
	
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
		
		args[1] = args[1].toLowerCase();
		
		for (String name : ((MemorySection) playersCfg.get("players")).getValues(false).keySet()) {
			if (name.startsWith(args[1])) {
				result.add(name);
			}
		}
		
		return result;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		String name = args[1].toLowerCase();
		
		int playerInfo = fromHex(playersCfg.getString("players." + name)) & 0xFFFFFF;
		if (playerInfo != 0) {
			playersCfg.set("players." + name, "0x0");
			sender.sendMessage(format("You set %s's info to 0.", args[1]));
		} else {
			playersCfg.set("players." + name, null);
			sender.sendMessage(format("You reset %s's info.", args[1]));
		}
		
		scboard.getObjective("ClanID").getScore(name).setScore(0);
		for (Entry<String, Object> entry2 : guildsCfg.getValues(false).entrySet()) {
			scboard.getObjective(format("T_%s", guildsCfg.getString(entry2.getKey() + ".engName")))
					.getScore(name).setScore(0);
		}
		
		saveCfgs();
		initCfgsToScoreboard(server.getPlayer(name), true);
	}

}
