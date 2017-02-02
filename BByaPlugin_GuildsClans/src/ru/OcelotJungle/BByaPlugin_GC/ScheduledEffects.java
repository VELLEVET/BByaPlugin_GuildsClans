package ru.OcelotJungle.BByaPlugin_GC;

/*******************************************
 *                                         *
 *   Выдача эффектов игрокам из гильдий    *
 *                                         *
 *******************************************/

import java.util.Locale;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ScheduledEffects implements Runnable {	
	
	private static final World world = Main.server.getWorld("world");
	private static boolean morning = false;

	public ScheduledEffects(Main plugin) {
		Main.server.getScheduler().cancelAllTasks();
		Main.server.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, Long.parseLong(Configs.mainCfg.getString("frequency").replaceAll("[^0-9]", "")));
	}
	
	@SuppressWarnings("deprecation")
	public void run() {
		
		// ���� ���� ���� �������� � ������ ����
		if(!morning && (world.getTime() >= 0) && (world.getTime() <= 200)) {
			morning = true;
			
		// ���� ���� ���� ������� � ������ �� ����
		} else if (morning && (world.getTime() > 200)) {
			morning = false;
		}
		
		for (Player player : Main.server.getOnlinePlayers()) {
			for (long effectInfo : Main.effectList) {
				
				// ���� (������ �������� � ������ ����) ��� ������ ����������
				if ( ((((effectInfo >> 6*8)&0xFF) == 2) && morning) || (((effectInfo >> 6*8)&0xFF) == 1) ) {
					
					String name = player.getName().toLowerCase(Locale.ENGLISH);
					int playerInfo = SomeStuff.fromHex(Configs.playersCfg.getString("players." + name));
					
					// ���� ����� ������� � ��������������� ������� � eventCheck = 0
					if ( (((playerInfo >> 1*8)&0xFF) == ((effectInfo >> 5*8)&0xFF)) && (Main.scboard.getObjective("eventCheck").getScore(name).getScore() == 0) ) {
						
						// ���� ������� ������ ��������� � �������� ������� �������
						if ( ((playerInfo&0xFF) >= ((effectInfo >> 1*8)&0xFF)) && ((playerInfo&0xFF) <= (effectInfo&0xFF)) ) {
							
							player.addPotionEffect(new PotionEffect(PotionEffectType.getById((int) ((effectInfo >> 3*8)&0xFF)), 432000, ((int) ((effectInfo >> 2*8)&0xFF))-1, true, false));
						}
					}
				}
			}
		}
	}
}