package ru.OcelotJungle.BByaPlugin_GC.Commands.Commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.OcelotJungle.BByaPlugin_GC.Configs;
import ru.OcelotJungle.BByaPlugin_GC.Main;
import ru.OcelotJungle.BByaPlugin_GC.ScheduledEffects;
import ru.OcelotJungle.BByaPlugin_GC.SomeStuff;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Manage.CommandInterface;

public class ReloadCommand implements CommandInterface {
	
	private static final int argumentCount = 1;
	private static final String usage = "reload",
								description = "reloads configs";
	
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
		
		Configs.reloadCfgs();
		SomeStuff.initCfgsToScoreboard();
		SomeStuff.initEffects();
		
		new ScheduledEffects(Main.plugin);
		
		for (Player player : Main.server.getOnlinePlayers()) {
			SomeStuff.rebuildPlayerNickname(player);
		}
		
		sender.sendMessage("BByaPlugin_GuildsClans configs reloaded.");
	}
	
}
