package ru.ocelotjungle.bbyaplugin_gc;

/*******************************************
 *                                         *
 *      Saving and reloading configs       *
 *                                         *
 *******************************************/

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static ru.ocelotjungle.bbyaplugin_gc.Main.plugin;

public class Configs {
	
	public static FileConfiguration mainCfg, playersCfg, clansCfg, guildsCfg;
	
	public static void saveCfgs() {
		try {
			playersCfg.save(new File(plugin.getDataFolder(), "players.yml"));
		} catch (IOException io) {
			Main.logger().severe(Utils.format("Cannot save players config to disk. Reason: %s", io.toString()));
		}
		
		// No need to save these cfgs
		/*try { Configs.clansCfg.save(new File(Main.plugin.getDataFolder(), "clans.yml")); }
		catch (IOException io) { Logging.errF("Cannot save clans config to disk. Reason: %s", io.toString()); }*/
		
		/*try { Configs.guildsCfg.save(new File(Main.plugin.getDataFolder(), "guilds.yml")); }
		catch (IOException io) { Logging.errF("Cannot save guilds config to disk. Reason: %s", io.toString()); }*/
		
		/*try { Configs.mainCfg.save(new File(Main.plugin.getDataFolder(), "config.yml")); }
		catch (IOException io) { Logging.errF("Cannot save main config to disk. Reason: %s", io.toString()); }*/
	}
	
	public static void reloadPlayersCfg() {
		File file = new File(plugin.getDataFolder(), "players.yml");
		if(!file.exists()) { plugin.saveResource(file.getName(), false); }
		playersCfg = YamlConfiguration.loadConfiguration(file);
	}
	
	public static void reloadClansCfg() {
		File file = new File(plugin.getDataFolder(), "clans.yml");
		if(!file.exists()) { plugin.saveResource(file.getName(), false); }
		clansCfg = YamlConfiguration.loadConfiguration(file);
	}
	
	public static void reloadGuildsCfg() {
		File file = new File(plugin.getDataFolder(), "guilds.yml");
		if(!file.exists()) { plugin.saveResource(file.getName(), false); }
		guildsCfg = YamlConfiguration.loadConfiguration(file);
	}
	
	public static void reloadMainCfg() {
		File file = new File(plugin.getDataFolder(), "config.yml");
		if(!file.exists()) { plugin.saveResource(file.getName(), false); }
		mainCfg = YamlConfiguration.loadConfiguration(file);
	}
	
	public static void reloadCfgs() {
		reloadPlayersCfg();
		reloadClansCfg();
		reloadGuildsCfg();
		reloadMainCfg();
	}

}

