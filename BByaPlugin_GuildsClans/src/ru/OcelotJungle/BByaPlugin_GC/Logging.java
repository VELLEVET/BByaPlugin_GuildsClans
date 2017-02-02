package ru.OcelotJungle.BByaPlugin_GC;

/*******************************************
 *                                         *
 *     Вывод логов и ошибок в консоль      *
 *                                         *
 *******************************************/


public class Logging {
	
	public static void log(Object Log) {
		Main.server.getLogger().info(Log==null? "null" : Log.toString());
	}
	
	public static void log(Object... Logs) {
		StringBuilder temp = new StringBuilder();
		for(Object Log : Logs) {
			temp.append(Log.toString());
		}
		log(temp);
	}
	
	public static void logF(String source, Object... Logs) {
		log(SomeStuff.format(source, Logs));
	}
	
	public static void err(Object Err) {
		Main.server.getLogger().severe(Err==null? "null" : Err.toString());
	}
	
	public static void err(Object... Errs) {
		StringBuilder temp = new StringBuilder();
		for(Object Err : Errs) {
			temp.append(Err.toString());
		}
		err(temp);
	}
	
	public static void errF(String source, Object... Errs) {
		err(SomeStuff.format(source, Errs));
	}
}
