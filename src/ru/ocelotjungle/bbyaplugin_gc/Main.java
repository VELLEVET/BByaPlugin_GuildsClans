package ru.ocelotjungle.bbyaplugin_gc;

/*******************************************
 *                                         *
 *         The main working class          *
 *                                         *
 *******************************************/

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;

import java.util.LinkedHashMap;
import java.util.logging.Logger;

import static ru.ocelotjungle.bbyaplugin_gc.Utils.checkObjectives;

public class Main extends JavaPlugin {
	public static org.bukkit.Server server;
	public static Scoreboard scboard;
	public static Main plugin;
	public static LinkedHashMap<Long, PotionEffect> effectList;
	
	public Main() { }

	public void onEnable() {		
		server = getServer();
		scboard = getServer().getScoreboardManager().getMainScoreboard();
		plugin = this;

		Configs.reloadCfgs();
		checkObjectives();

		scboard.resetScores("@p");
		
		Utils.initEffects();
		Utils.initCfgsToScoreboard(true);

		new CommandManager(this);

		new PlayerJoinEventListener(this);
		new EffectScheduler(this);

		logger().info("Enabled");
	}

	public void onDisable() {
		server.getScheduler().cancelTasks(this);
		logger().info("Disabled");
	}

	public static Logger logger() {
		return plugin.getLogger();
	}
}