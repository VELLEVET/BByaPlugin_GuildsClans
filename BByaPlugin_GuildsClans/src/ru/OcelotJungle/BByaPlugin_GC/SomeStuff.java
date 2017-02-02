package ru.OcelotJungle.BByaPlugin_GC;

/*******************************************
 *                                         *
 *           Ничего конкретного            *
 *                                         *
 *******************************************/

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

public class SomeStuff {
	
	private static FileConfiguration playersCfg, guildsCfg, clansCfg;
	private static Scoreboard scboard;
	
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

	private static void reloadCfgs() {
		Configs.reloadCfgs();
		playersCfg = Configs.playersCfg;
		guildsCfg = Configs.guildsCfg;
		clansCfg = Configs.clansCfg;
		scboard = Main.scboard;
	}
	
	public static void initCfgsToScoreboard() {
		
		reloadCfgs();
		
		if(!playersCfg.contains("players") || ((MemorySection) playersCfg.get("players")).getValues(false).size() == 0) {
			Logging.log("[BByaPlugin_GuildsClans] List of players is empty");
			return;
		}
		
		for (Map.Entry<String, Object> entry : ((MemorySection) playersCfg.get("players")).getValues(false).entrySet()) {
			
			String name = entry.getKey().toLowerCase(Locale.ENGLISH);
			int playerInfo = fromHex(entry.getValue().toString());
			
			// �������������� ����-���������� playerInfo
			//scboard.getObjective("playerInfo").getScore(name).setScore(playerInfo); 
			
			for (Entry<String, Object> entry2 : ((MemorySection) guildsCfg.get("guilds")).getValues(false).entrySet()) {
				scboard.getObjective(format("T_%s", guildsCfg.getString(format("guilds.%s.engName", entry2.getKey()))))
						.getScore(name).setScore(0);
			}
			
			// ���� ����� ������� � �����-���� �������, ���������� � �������
			if ((playerInfo&0xFF00) != 0) {
				if (guildsCfg.getString(format("guilds.%d.engName", ((playerInfo>>8)&0xFF))) != null) {
					scboard.getObjective(format("T_%s", guildsCfg.getString(format("guilds.%d.engName", ((playerInfo>>8)&0xFF)))))
							.getScore(name).setScore(playerInfo&0xFF);
				} else {
					Logging.errF("There's no guild with this ID (%s; %s)", name, Integer.toHexString(((playerInfo>>8)&0xFF)));
				}
			}
			
			
			// ���� ����� ������� � �����-���� �����, ���������� � �������
			if ((playerInfo&0xFF0000) != 0) {
				if (clansCfg.getString(format("clans.%d.label", (playerInfo>>16)&0xFF)) != null) {
					scboard.getObjective("ClanID").getScore(name).setScore((playerInfo>>16)&0xFF);
				} else {
					Logging.errF("There's no clan with this ID (%s; %d)", name, ((playerInfo>>16)&0xFF));
				}
			}
		}
	}
	
	public static void initEffects() {
		reloadCfgs();
		
		Main.effectList = new LinkedHashSet<Long>();
		
		for(String entry : ((MemorySection) guildsCfg.get("guilds")).getValues(false).keySet()) {
			for(String entry2 : ((MemorySection) guildsCfg.get(format("guilds.%s.effects", entry))).getValues(false).keySet()) {
				
				MemorySection effectInfo = (MemorySection) guildsCfg.get(format("guilds.%s.effects.%s", entry, entry2));
				
				Main.effectList.add(
						((effectInfo.getBoolean("morning")?0x02L:0x01L) << 6*8) | 
						(Long.parseLong(entry) << 5*8) |
						(Long.parseLong(entry2) << 4*8) |
						(PotionEffectType.getByName(effectInfo.getString("name")).hashCode() << 3*8) |
						(effectInfo.getLong("level") << 2*8) |
						((int) effectInfo.getList("levels").get(0) << 1*8) |
						((int)effectInfo.getList("levels").get(1)));
			}
		}
	}
	
	public static void rebuildPlayerNickname(Player player) {
		if (player == null) return;
		
		Configs.reloadPlayersCfg();
		
		String name = player.getName().toLowerCase(Locale.ENGLISH);
		
		int playerInfo = fromHex(playersCfg.getString("players." + name)) & 0xFFFFFF;
		
		StringBuilder newName = new StringBuilder();
		
		// ���� ����� ������� � �����-���� �����
		if ((playerInfo&0xFF0000) != 0 && clansCfg.getString(format("clans.%d.label", ((playerInfo>>2*8)&0xFF))) != null) {
			newName.append(ChatColor.valueOf(clansCfg.getString(format("clans.%d.color",((playerInfo>>2*8)&0xFF))).toUpperCase(Locale.ENGLISH)))
			.append(format("[%s] ", clansCfg.getString(format("clans.%d.label", ((playerInfo>>2*8)&0xFF)))))
			.append(ChatColor.RESET);
		}
		
		// ���� ����� ������� � �����-���� ������� � ����
		if (Main.scboard.getEntryTeam(player.getName()) != null) {
			newName.append(Main.scboard.getEntryTeam(player.getName()).getPrefix())
			.append(player.getName());
			
			// ���� ID � ������� ������� ��������� � ������� ����������
			if ((playerInfo&0xFF00) != 0 && (playerInfo&0xFF) != 0 && guildsCfg.getString(format("guilds.%d.engName", ((playerInfo>>8)&0xFF))) != null) {
				newName.append(ChatColor.WHITE + " | ")
				.append(ChatColor.valueOf(guildsCfg.getString(format("guilds.%d.postfix.color", ((playerInfo>>8)&0xFF))).toUpperCase(Locale.ENGLISH)))
				.append(guildsCfg.getString(format("guilds.%d.postfix.symbol", ((playerInfo>>8)&0xFF))).toUpperCase(Locale.ENGLISH))
				.append("-" + toRoman(playerInfo&0xFF) + ChatColor.RESET);
			}
		} else {
			
			// ���� ID � ������� ������� ��������� � ������� ����������
			if ((playerInfo&0xFF00) != 0 && (playerInfo&0xFF) != 0 && guildsCfg.getString(format("guilds.%d.engName", ((playerInfo>>8)&0xFF))) != null) {
				newName.append(ChatColor.valueOf(guildsCfg.getString(format("guilds.%d.postfix.color", ((playerInfo>>8)&0xFF))).toUpperCase(Locale.ENGLISH)));
			}
			
			newName.append(player.getName());
			
			// ���� ID � ������� ������� ��������� � ������� ���������� (��, � 3 ���� �������� ����� ���������� ��������)
			if ((playerInfo&0xFF00) != 0 && (playerInfo&0xFF) != 0 && guildsCfg.getString(format("guilds.%d.engName", ((playerInfo>>8)&0xFF))) != null) {
				newName.append(ChatColor.WHITE + " | ")
				.append(ChatColor.valueOf(guildsCfg.getString(format("guilds.%d.postfix.color", ((playerInfo>>8)&0xFF))).toUpperCase(Locale.ENGLISH)))
				.append(guildsCfg.getString(format("guilds.%d.postfix.symbol", ((playerInfo>>8)&0xFF))).toUpperCase(Locale.ENGLISH))
				.append("-" + toRoman(playerInfo&0xFF) + ChatColor.RESET);
			}
		}
		
		player.setPlayerListName(newName.toString());
	}
}