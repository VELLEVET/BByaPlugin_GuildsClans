package ru.OcelotJungle.BByaPlugin_GC;

/*******************************************
 *                                         *
 *   Сохранение и перезагрузка конфигов    *
 *                                         *
 *******************************************/

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configs {
	
	public static FileConfiguration mainCfg, playersCfg, clansCfg, guildsCfg;
	
	public static void saveCfgs() {
		try { Configs.playersCfg.save(new File(Main.plugin.getDataFolder(), "players.yml")); }
		catch (IOException io) { Logging.errF("Cannot save players config to disk. Reason: %s", io.toString()); }
		
		// Эти конфиги сохранять не нужно
		/*try { Configs.clansCfg.save(new File(Main.plugin.getDataFolder(), "clans.yml")); }
		catch (IOException io) { Logging.errF("Cannot save clans config to disk. Reason: %s", io.toString()); }*/
		
		/*try { Configs.guildsCfg.save(new File(Main.plugin.getDataFolder(), "guilds.yml")); }
		catch (IOException io) { Logging.errF("Cannot save guilds config to disk. Reason: %s", io.toString()); }*/
		
		/*try { Configs.mainCfg.save(new File(Main.plugin.getDataFolder(), "config.yml")); }
		catch (IOException io) { Logging.errF("Cannot save main config to disk. Reason: %s", io.toString()); }*/
	}
	
	public static void reloadPlayersCfg() {
		File file = new File(Main.plugin.getDataFolder(), "players.yml");
		if(!file.exists()) { Main.plugin.saveResource(file.getName(), false); }
		Configs.playersCfg = YamlConfiguration.loadConfiguration(file);
	}
	
	public static void reloadClansCfg() {
		File file = new File(Main.plugin.getDataFolder(), "clans.yml");
		if(!file.exists()) { Main.plugin.saveResource(file.getName(), false); }
		Configs.clansCfg = YamlConfiguration.loadConfiguration(file);
	}
	
	public static void reloadGuildsCfg() {
		File file = new File(Main.plugin.getDataFolder(), "guilds.yml");
		if(!file.exists()) { Main.plugin.saveResource(file.getName(), false); }
		Configs.guildsCfg = YamlConfiguration.loadConfiguration(file);
	}
	
	public static void reloadMainCfg() {
		File file = new File(Main.plugin.getDataFolder(), "config.yml");
		if(!file.exists()) { Main.plugin.saveResource(file.getName(), false); }
		Configs.mainCfg = YamlConfiguration.loadConfiguration(file);
	}
	
	public static void reloadCfgs() {
		reloadPlayersCfg();
		reloadClansCfg();
		reloadGuildsCfg();
		reloadMainCfg();
	}

}
