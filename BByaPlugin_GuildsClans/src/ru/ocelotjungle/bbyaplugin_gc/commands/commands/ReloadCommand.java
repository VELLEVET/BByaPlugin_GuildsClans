package ru.ocelotjungle.bbyaplugin_gc.commands.commands;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.reloadCfgs;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.checkObjectives;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.initCfgsToScoreboard;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.initEffects;

import java.util.List;

import org.bukkit.command.CommandSender;

import ru.ocelotjungle.bbyaplugin_gc.EffectScheduler;
import ru.ocelotjungle.bbyaplugin_gc.Main;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandInterface;

public class ReloadCommand implements CommandInterface {
	
	private static final int ARGUMENT_COUNT = 1;
	private static final String USAGE = "reload",
								DESCRIPTION = "reloads configs";
	
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
		
		reloadCfgs();
		checkObjectives();
		initCfgsToScoreboard(true);
		initEffects();
		
		new EffectScheduler(Main.plugin);
		
		sender.sendMessage("BByaPlugin_GuildsClans configs reloaded.");
	}
	
}
