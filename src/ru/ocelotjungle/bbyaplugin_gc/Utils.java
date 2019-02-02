package ru.ocelotjungle.bbyaplugin_gc;

/*******************************************
 *                                         *
 *           Nothing specific              *
 *                                         *
 *******************************************/

import org.bukkit.ChatColor;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import static ru.ocelotjungle.bbyaplugin_gc.Configs.*;
import static ru.ocelotjungle.bbyaplugin_gc.Main.*;

public abstract class Utils {
	
	public static enum EffectType {
		NULL(null),
		SPEED(PotionEffectType.SPEED),
		SLOWNESS(PotionEffectType.SLOW),
		HASTE(PotionEffectType.FAST_DIGGING),
		MINING_FATIGUE(PotionEffectType.SLOW_DIGGING),
		STRENGTH(PotionEffectType.INCREASE_DAMAGE),
		INSTANT_HEALTH(PotionEffectType.HEAL),
		INSTANT_DAMAGE(PotionEffectType.HARM),
		JUMP_BOOST(PotionEffectType.JUMP),
		NAUSEA(PotionEffectType.CONFUSION),
		REGENERATION(PotionEffectType.REGENERATION),
		RESISTANCE(PotionEffectType.DAMAGE_RESISTANCE),
		FIRE_RESISTANCE(PotionEffectType.FIRE_RESISTANCE),
		WATER_BREATHING(PotionEffectType.WATER_BREATHING),
		INVISIBILITY(PotionEffectType.INVISIBILITY),
		BLINDNESS(PotionEffectType.BLINDNESS),
		NIGHT_VISION(PotionEffectType.NIGHT_VISION),
		HUNGER(PotionEffectType.HUNGER),
		WEAKNESS(PotionEffectType.WEAKNESS),
		POISON(PotionEffectType.POISON),
		WITHER(PotionEffectType.WITHER),
		HEALTH_BOOST(PotionEffectType.HEALTH_BOOST, true),
		ABSORPTION(PotionEffectType.ABSORPTION, true),
		SATURATION(PotionEffectType.SATURATION),
		GLOWING(PotionEffectType.GLOWING),
		LEVITATION(PotionEffectType.LEVITATION),
		LUCK(PotionEffectType.LUCK),
		UNLUCK(PotionEffectType.UNLUCK);
		
		private final PotionEffectType spigotPotionEffectType;
		private final boolean isSafeMorningRegiveNeeded;

		private EffectType(PotionEffectType type) {
			this(type, false);
		}
		
		private EffectType(PotionEffectType type, boolean saveRegive) {
			this.spigotPotionEffectType = type;
			this.isSafeMorningRegiveNeeded = saveRegive;
		}
		
		public static PotionEffectType getByName(String name) {
			try {
				return valueOf(name.toUpperCase()).getEffectType();
			} catch (IllegalArgumentException iae) {
				Main.logger().warning("There is no effect with name " + name + ", used Glowing.");
				return PotionEffectType.GLOWING;
			}
		}
		
		public boolean getSafeMorningRegive() {
			return isSafeMorningRegiveNeeded;
		}
		
		public PotionEffectType getEffectType() {
			return spigotPotionEffectType;
		}
	}
	
	public static String format(String source, Object... args) {
		return String.format(source, args);
	}
	
	public static String toRoman(int number) {
		StringBuilder result = new StringBuilder();
		Object values[][] = {{"C",100},{"XC",90},{"XC",90},{"L",50},{"XL",40},{"X",10},{"IX",9},{"V",5},{"IV",4},{"I",1}};
		
		for (Object value[] : values) {
			while(number >= (int) value[1]) {
				result.append((String) value[0]);
				number -= (int) value[1];
			}
		}
		
		return result.toString();
	}
	
	public static String toHex(int number) {
		return "0x" + Integer.toHexString(number).toUpperCase();
	}

	public static int fromHex(String hex) {
		return hex==null? 0 : Integer.parseInt(hex.toUpperCase(Locale.ENGLISH).replaceAll("(0X|H| |[^0-9A-F])", ""), 16);
	}
	
	public static void initCfgsToScoreboard(Player player, boolean isRebuildNeeded) {
		
		if (player == null) return;
		
		reloadCfgs();
		
		if(!playersCfg.contains("players") || ((MemorySection) playersCfg.get("players")).getValues(false).size() == 0) {
			Main.logger().info("List of players is empty");
			return;
		}
		
		String name = player.getName().toLowerCase(),
				fullName = player.getName();
		int playerInfo = fromHex(playersCfg.getString("players." + name)),
			clanId = (playerInfo>>2*8)&0xFF,
			guildId = (playerInfo>>1*8)&0xFF,
			level = playerInfo&0xFF;
		
		for (String guild : guildsCfg.getValues(false).keySet()) {
			String objectiveName = "T_" + guildsCfg.getString(guild + ".engName");
			
			if (scboard.getObjective(objectiveName) == null) {
				scboard.registerNewObjective(objectiveName, "dummy");
			}
			
			scboard.getObjective(objectiveName).getScore(fullName).setScore(0);
		}
		
		// If player is a member of any guild, which is in the config
		if (guildId != 0) {
			if (guildsCfg.contains(guildId + "")) {
				scboard.getObjective("T_" + guildsCfg.getString(guildId + ".engName"))
						.getScore(fullName).setScore(level);
				
			} else {
				Main.logger().warning(format("There's no guild with this ID (%s; %d)", name, guildId));
			}
		}
		
		
		// If player is a member of any clan, which is in the config
		if (clanId == 0 || clansCfg.contains(clanId + "")) {
			scboard.getObjective("ClanID").getScore(fullName).setScore(clanId);
			
		} else {
			Main.logger().warning(format("There's no clan with this ID (%s; %d)", name, clanId));
		}
		
		if (isRebuildNeeded) {
			rebuildPlayerNickname(player);
		}
	}
	
	public static void initCfgsToScoreboard(boolean isOnlinePlayers) {
		
		checkObjectives();
		
		if (isOnlinePlayers) {
			for (Player player : server.getOnlinePlayers()) {
				initCfgsToScoreboard(player, true);
			}
		} else {
			initCfgsToScoreboard();
		}
	}
	
	public static void initCfgsToScoreboard() {
		
		reloadCfgs();
		
		if(!playersCfg.contains("players") || ((MemorySection) playersCfg.get("players")).getValues(false).size() == 0) {
			Main.logger().info("List of players is empty");
			return;
		}
		
		for (Map.Entry<String, Object> player : ((MemorySection) playersCfg.get("players")).getValues(false).entrySet()) {
			
			String name = player.getKey().toLowerCase(Locale.ENGLISH),
					fullName = server.getPlayer(name)!=null ? server.getPlayer(name).getName() : name;
			int playerInfo = fromHex(player.getValue().toString()),
				clanId = (playerInfo>>2*8)&0xFF,
				guildId = (playerInfo>>1*8)&0xFF,
				level = playerInfo&0xFF;
			
			// Unusued Scoreboard objective "playerInfo"
			//scboard.getObjective("playerInfo").getScore(name).setScore(playerInfo); 
			
			for (Entry<String, Object> guild : guildsCfg.getValues(false).entrySet()) {
				String objectiveName = format("T_%s", guildsCfg.getString(guild.getKey() + ".engName"));
				if (scboard.getObjective(objectiveName) == null) {
					scboard.registerNewObjective(objectiveName, "dummy");
				}
				scboard.getObjective(objectiveName).getScore(fullName).setScore(0);
			}
			
			// If player is a member of any guild, which is in the config
			if (guildId != 0) {
				if (guildsCfg.contains(guildId + "")) {
					scboard.getObjective(format("T_%s", guildsCfg.getString(guildId + ".engName")))
							.getScore(fullName).setScore(level);
					
				} else {
					Main.logger().warning(format("There's no guild with this ID (%s; %d)", name, guildId));
				}
			}
			
			
			// If player is a member of any clan, which is in the config
			if (clanId != 0) {
				if (clansCfg.contains(clanId + "")) {
					scboard.getObjective("ClanID").getScore(fullName).setScore(clanId);
					
				} else {
					Main.logger().warning(format("There's no clan with this ID (%s; %d)", name, clanId));
				}
			}
		}
	}
	
	public static void initEffects() {
		reloadCfgs();
		
		effectList = new LinkedHashMap<Long, PotionEffect>();
		
		for(String guild : guildsCfg.getValues(false).keySet()) {
			try { Short.parseShort(guild); } 
			catch (NumberFormatException nfe) {
				Main.logger().warning(format("There's incorrect value of guild number (%s).", guild));
				continue;
			}
			
			if (!guildsCfg.contains(guild + ".effects")) {
				continue;
			}
			
			for(String effectNumber : ((MemorySection) guildsCfg.get(guild + ".effects")).getValues(false).keySet()) {
				try { Short.parseShort(effectNumber); } 
				catch (NumberFormatException nfe) {
					Main.logger().warning(format("There's incorrect value of effect number (%s; %s).", guild, effectNumber));
					continue;
				}
				
				MemorySection effectInfo = (MemorySection) guildsCfg.get(guild + ".effects." + effectNumber);
				try {
					for (Entry<String, Object> effect : ((MemorySection) effectInfo.get("levels")).getValues(false).entrySet()) {
						try { Short.parseShort(effect.getKey()); } 
						catch (NumberFormatException nfe) {
							Main.logger().warning(format("There's incorrect value of effect level (%s.%s; %s).", guild, effectNumber, effect.getKey()));
							continue;
						}
						
						int minLevel = 0, maxLevel = 255;
						
						if (effect.getValue() instanceof Integer && (int) effect.getValue() > 0) {
							maxLevel = (int) effect.getValue();
							
						} else if (effect.getValue() instanceof ArrayList) {
							ArrayList<Object> value = (ArrayList<Object>) effect.getValue();
							
							if (value.size() == 1 && (int) value.get(0) > 0) {
								minLevel = (int) value.get(0);
								 
							} else if (value.size() >= 2) {
								minLevel = (int) value.get(0);
								maxLevel = (int) value.get(1);
							}
							
						}
						
						effectList.put(
								(((effectInfo.getBoolean("morning")?0x02L:0x01L) << 6*8) | 	// Is it morning effect or no
								(Long.parseLong(guild) << 5*8) | 			// Guild ID
								(Long.parseLong(effectNumber) << 4*8) |		// Effect ID
								(Long.parseLong(effect.getKey()) << 3*8) |	// Effect level
								(minLevel << 2*8) |							// Min guild level
								(maxLevel << 1*8) |							// Max guild level
								(EffectType.valueOf(effectInfo.getString("name").toUpperCase()).getSafeMorningRegive() ? 0x01L : 0x00L)),
								
								new PotionEffect(
								(EffectType.getByName(effectInfo.getString("name").toUpperCase())), 
								mainCfg.getInt("effectDuration"),
								Integer.parseInt(effect.getKey()) - 1, true, false));
					}
				} catch (NullPointerException npe) {
					Main.logger().warning("Not found any effect at " + guild + "." + effectNumber);
				}
			}
		}
	}
	
	public static void rebuildPlayerNickname(Player player) {
		if (player == null) return;
		
		reloadPlayersCfg();
		
		Locale eng = Locale.ENGLISH;
		String name = player.getName().toLowerCase(eng);
		
		int playerInfo = fromHex(playersCfg.getString("players." + name)) & 0xFFFFFF,
			clan = (playerInfo>>2*8)&0xFF,
			guild = (playerInfo>>1*8)&0xFF,
			level = playerInfo&0xFF;
		
		StringBuilder newName = new StringBuilder();
		
		boolean hasClan = clan != 0 && clansCfg.contains(clan + ".label"),
				hasGuild = guild != 0 && level != 0 && guildsCfg.contains(guild + ".engName"),
				hasTeam = scboard.getEntryTeam(player.getName()) != null;
		
		// If player is a member of any clan AND this clan exists
		if (hasClan) {
			newName.append(ChatColor.valueOf(clansCfg.getString(clan + ".color").toUpperCase(eng)))
			.append(format("[%s] ", clansCfg.getString(clan + ".label")))
			.append(ChatColor.RESET);
		}
		
		// If player have any Scoreboard team
		if (hasTeam) {
			newName.append(Main.scboard.getEntryTeam(player.getName()).getPrefix());
			
		// If guild ID and level isn't equal to 0 AND this guild exists
		} else if (hasGuild) {
			newName.append(ChatColor.valueOf(guildsCfg.getString(guild + ".postfix.color").toUpperCase(eng)));
			
		}
		
		newName.append(player.getName());
		
		// If player have any Scoreboard team
		if (hasTeam) {
			newName.append(Main.scboard.getEntryTeam(player.getName()).getSuffix());
		}
		
		// If guild ID and level isn't equal to 0 AND this guild exists
		if (hasGuild) {
			newName.append(ChatColor.WHITE + " | ")
			.append(ChatColor.valueOf(guildsCfg.getString(guild + ".postfix.color").toUpperCase(eng)))
			.append(guildsCfg.getString(guild + ".postfix.symbol").toUpperCase(eng))
			.append("-" + toRoman(level) + ChatColor.RESET);
		}
		
		player.setPlayerListName(newName.toString());
	}
	
	public static void checkObjectives() {
		if (scboard.getObjective("Emerald_money") == null) scboard.registerNewObjective("Emerald_money", "dummy");
		if (scboard.getObjective("ExpBottle") == null) scboard.registerNewObjective("ExpBottle", "dummy");
		if (scboard.getObjective("EventCheck") == null) scboard.registerNewObjective("EventCheck", "dummy");
		
		if (scboard.getObjective("ClanID") != null) scboard.getObjective("ClanID").unregister();
		scboard.registerNewObjective("ClanID", "dummy");

		for (String guild : guildsCfg.getValues(false).keySet()) {
			String objectiveName = "T_" + guildsCfg.getString(guild + ".engName");
			
			if (scboard.getObjective(objectiveName) != null) {
				scboard.getObjective(objectiveName).unregister();
			}
			scboard.registerNewObjective(objectiveName, "dummy");
		}
	}
}