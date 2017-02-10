package ru.ocelotjungle.bbyaplugin_gc.commands.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.clansCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.playersCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Main.server;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.format;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.fromHex;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandInterface;

public class GetClanCommand implements CommandInterface {

	private static final int ARGUMENT_COUNT = 2;
	private static final String USAGE = "getclan <player>",
								DESCRIPTION = "returns player's clan";
	
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
		String name = args[1].toLowerCase();
		
		int clan = (fromHex(playersCfg.getString("players." + name))>>2*8) & 0xFF;
		sender.sendMessage(format("%s's clan is (%s; %d).", args[1], clansCfg.getString(clan + ".label"), clan));
	}
}
