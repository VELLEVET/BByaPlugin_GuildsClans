package ru.ocelotjungle.bbyaplugin_gc;

/*******************************************
 *                                         *
 *         The main working class          *
 *                                         *
 *******************************************/

import java.util.LinkedHashSet;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandManager;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.TabComplete;

public class Main extends JavaPlugin {

	public static org.bukkit.Server server;
	public static Scoreboard scboard;
	public static Main plugin;
	public static LinkedHashSet<Long> effectList;

	public void onEnable() {
		//ABCBDBHEJE111111111111
		server = getServer();
		scboard = getServer().getScoreboardManager().getMainScoreboard();
		plugin = this;

		scboard.resetScores("@p");
		scboard.resetScores("@a");

		Configs.reloadCfgs();
		Utils.initEffects();
		Utils.initCfgsToScoreboard();

		new CommandManager(this);
		new TabComplete(this);
		new PlayerJoinEventListener(this);
		new ScheduledEffects(this);

		Logging.log("[BByaPlugin_GuildsClans] Enabled");
	}

	public void onDisable() {
		Logging.log("[BByaPlugin_GuildsClans] Disabled");
	}
}