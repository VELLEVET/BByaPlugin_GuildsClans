package ru.ocelotjungle.bbyaplugin_gc.commands.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;

import ru.ocelotjungle.bbyaplugin_gc.Configs;
import ru.ocelotjungle.bbyaplugin_gc.Main;
import ru.ocelotjungle.bbyaplugin_gc.Utils;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandInterface;

public class ResetInfoCommand implements CommandInterface {
	
	private static final int argumentCount = 2;
	private static final String usage = "resetinfo <player>",
								description = "resets player's info";
	
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
		List<String> result = new ArrayList<String>();
		
		for (String name : ((MemorySection) Configs.playersCfg.get("players")).getValues(false).keySet()) {
			if (name.startsWith(args[1])) {
				result.add(name);
			}
		}
		
		return result;
	}

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		String name = args[1].toLowerCase();
		
		if (Utils.fromHex(Configs.playersCfg.getString("players." + name)) != 0) {
			Configs.playersCfg.set("players." + name, "0x0");
			sender.sendMessage(Utils.format("You set %s's info to 0.", args[1]));
		} else {
			Configs.playersCfg.set("players." + name, null);
			sender.sendMessage(Utils.format("You reset %s's info.", args[1]));
		}
		
		Configs.saveCfgs();
		Utils.rebuildPlayerNickname(Main.plugin.getServer().getPlayer(args[1]));
		Utils.initCfgsToScoreboard();
	}

}
