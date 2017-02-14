package ru.ocelotjungle.bbyaplugin_gc;

/*******************************************
 *                                         *
 *         The main working class          *
 *                                         *
 *******************************************/

import static ru.ocelotjungle.bbyaplugin_gc.Utils.checkObjectives;

import java.util.LinkedHashMap;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;

import ru.ocelotjungle.bbyaplugin_gc.commands.manage.CommandManager;
import ru.ocelotjungle.bbyaplugin_gc.commands.manage.TabComplete;

public class Main extends JavaPlugin {

	public static org.bukkit.Server server;
	public static Scoreboard scboard;
	public static Main plugin;
	public static LinkedHashMap<Long, PotionEffect> effectList;
	
	public Main() {
		getLogger().setLevel(Level.OFF);
	}

	public void onEnable() {		
		server = getServer();
		scboard = getServer().getScoreboardManager().getMainScoreboard();
		plugin = this;

		Configs.reloadCfgs();
		checkObjectives();

		scboard.resetScores("@p");
		
		Utils.initEffects();
		Utils.initCfgsToScoreboard();

		new CommandManager(this);
		new TabComplete(this);
		new PlayerJoinEventListener(this);
		new EffectScheduler(this);
		
		Logger.log("[BByaPlugin_GuildsClans] Enabled");
	}

	public void onDisable() {
		Logger.log("[BByaPlugin_GuildsClans] Disabled");
	}
}