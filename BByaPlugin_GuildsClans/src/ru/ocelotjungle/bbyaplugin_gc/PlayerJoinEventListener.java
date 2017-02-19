package ru.ocelotjungle.bbyaplugin_gc;

/*******************************************
 *                                         *
 *          Player join handler            *
 *                                         *
 *******************************************/

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener implements Listener {

	public PlayerJoinEventListener(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public boolean onPlayerJoined(PlayerJoinEvent evt) {
		Utils.initCfgsToScoreboard(evt.getPlayer(), true);
		return true;
	}
}
