package ru.OcelotJungle.BByaPlugin_GC;

/*******************************************
 *                                         *
 *         Основной рабочий класс          *
 *                                         *
 *******************************************/


import java.util.LinkedHashSet;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import ru.OcelotJungle.BByaPlugin_GC.Commands.Manage.CommandManager;
import ru.OcelotJungle.BByaPlugin_GC.Commands.Manage.TabComplete;

public class Main extends JavaPlugin {
	
	public static org.bukkit.Server server;
	public static Scoreboard scboard;
	public static Main plugin;
	public static LinkedHashSet<Long> effectList;
	
	public void onEnable() {
		
		server = getServer();
		scboard = getServer().getScoreboardManager().getMainScoreboard();
		plugin = this;

		scboard.resetScores("@p");
		scboard.resetScores("@a");
		
		Configs.reloadCfgs();
		SomeStuff.initEffects();
		SomeStuff.initCfgsToScoreboard();
		
		new CommandManager(this);
		new TabComplete(this);
		new OnPlayerJoined(this);
		new ScheduledEffects(this);
		
		Logging.log("[BByaPlugin_GuildsClans] Enabled");
	}
	
	public void onDisable() {
		Logging.log("[BByaPlugin_GuildsClans] Disabled");
	}
}