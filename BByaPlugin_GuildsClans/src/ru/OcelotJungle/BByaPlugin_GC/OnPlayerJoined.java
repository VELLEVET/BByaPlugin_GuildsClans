package ru.OcelotJungle.BByaPlugin_GC;

/*******************************************
 *                                         *
 *     Обработка события входа игрока      *
 *                                         *
 *******************************************/

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoined implements Listener {
	
	public OnPlayerJoined(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public boolean onPlayerJoined(PlayerJoinEvent evt) {
		SomeStuff.rebuildPlayerNickname(evt.getPlayer());
		return true;
	}
}
