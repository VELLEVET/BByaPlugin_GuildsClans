package ru.ocelotjungle.bbyaplugin_gc;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.Locale;
import java.util.Map.Entry;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.mainCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Configs.playersCfg;
import static ru.ocelotjungle.bbyaplugin_gc.Main.*;
import static ru.ocelotjungle.bbyaplugin_gc.Utils.fromHex;

/*******************************************
 *                                         *
 *    Effect issue to players in guilds    *
 *                                         *
 *******************************************/

public class EffectScheduler implements Runnable {

	private static final World WORLD = server.getWorld("world");
	private static boolean morning = false;
	private static long frequency = Long.parseLong(mainCfg.getString("frequency").replaceAll("[^0-9]", ""));
	
	public EffectScheduler(Main plugin) {
		server.getScheduler().cancelTasks(plugin);
		server.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, frequency);
	}

	public void run() {
		
		// If morning=false AND it's morning now
		if (!morning && (WORLD.getTime() >= 0) && (WORLD.getTime() <= frequency*2)) {
			morning = true;

			// If morning=false AND it isn't morning now
		} else if (morning && (WORLD.getTime() > frequency*2)) {
			morning = false;
		}
		
		for (Player player : Main.server.getOnlinePlayers()) {
			Utils.initCfgsToScoreboard(player, true);

			for (Entry<Long, PotionEffect> effectEntry : effectList.entrySet()) {
				short effectInfo[] = {
						(short) ((effectEntry.getKey() >> 6*8) & 0xFF), // 0: Is morning
						(short) ((effectEntry.getKey() >> 5*8) & 0xFF), // 1: Guild ID
						(short) ((effectEntry.getKey() >> 3*8) & 0xFF), // 2: Effect level
						(short) ((effectEntry.getKey() >> 2*8) & 0xFF), // 3: Min guild level
						(short) ((effectEntry.getKey() >> 1*8) & 0xFF),	// 4: Max guild level
						(short) ((effectEntry.getKey() >> 0*8) & 0xFF)};// 5: Has safe morning regive
				
				// If (effect must be issued at the morning AND it's morning now)
				// OR effect must be issued everytime
				if ((effectInfo[0] == 2 && morning) || effectInfo[0] == 1) {

					String name = player.getName().toLowerCase(Locale.ENGLISH);
					int playerInfo = fromHex(playersCfg.getString("players." + name));
					
					// If effect's guild = player's guild AND EventCheck = 0
					if ((((playerInfo >> 1*8) & 0xFF) == effectInfo[1]) &&
							(scboard.getObjective("EventCheck").getScore(player.getName()).getScore() == 0)) {

						// If player's level upper or equal to min effect level
						// AND player's level lower or equal to max effect level
						if (((playerInfo & 0xFF) >= effectInfo[3]) && ((playerInfo & 0xFF) <= effectInfo[4])) {
							
							PotionEffect playerPotionEffect = player.getPotionEffect(effectEntry.getValue().getType());

							// If player haven't this effect OR
							// player's effect have now only <=24000 ticks (20m = 1 game day) OR
							// player's effect have less level
							if( playerPotionEffect == null ||
								(playerPotionEffect.getDuration() <= 24000 && effectInfo[5] == 1) ||
								(effectInfo[5] == 0) ||
								(playerPotionEffect.getAmplifier() < effectEntry.getValue().getAmplifier())) {
								
								player.removePotionEffect(effectEntry.getValue().getType());
								player.addPotionEffect(effectEntry.getValue());
							}
						}
					}
				}
			}
		}
	}
}