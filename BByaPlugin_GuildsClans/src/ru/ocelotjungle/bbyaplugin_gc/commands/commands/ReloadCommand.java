package ru.ocelotjungle.bbyaplugin_gc.commands.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.ocelotjungle.bbyaplugin_gc.Configs;
import ru.ocelotjungle.bbyaplugin_gc.Main;
import ru.ocelotjungle.bbyaplugin_gc.EffectScheduler;
import ru.ocelotjungle.bbyaplugin_gc.Utils;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandInterface;

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
		Utils.initCfgsToScoreboard();
		Utils.initEffects();
		
		new EffectScheduler(Main.plugin);
		
		for (Player player : Main.server.getOnlinePlayers()) {
			Utils.rebuildPlayerNickname(player);
		}
		
		sender.sendMessage("BByaPlugin_GuildsClans configs reloaded.");
	}
	
}
