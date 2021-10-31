package me.lafive.apollo.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.entity.Player;

import me.lafive.apollo.Apollo;

public class LogUtil {
	
	public static void addLog(Player player, String check, String subCheck, String verbose, long vl, double ping) {
		File path = new File(Apollo.getInstance().getDataFolder() + "/logs/" + player.getUniqueId() + ".txt");
		File dir = new File(Apollo.getInstance().getDataFolder() + "/logs/");
		dir.mkdirs();
		if (!path.exists()) {
			try {
				path.createNewFile();
			} catch (IOException ex) {
				System.out.println("[Apollo] An error ocurred while saving the log file for " + player.getName() + "." + " (IOException_1)");
			}
		}
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();  
			String logToAdd = "[" + dtf.format(now) + "] " + player.getName() + " failed " + check + "[" + subCheck + "] PING:" + ping + " [" + "Verbose:" + verbose + "]";
			FileWriter fw = new FileWriter(path, true);
			PrintWriter pw = new PrintWriter(fw);
			pw.println(logToAdd);
			pw.flush();
			pw.close();
			fw.close();
			
		} catch (FileNotFoundException ex) {
			System.out.println("[Apollo] An error ocurred while saving the log file for " + player.getName() + "." + " (FileNotFoundException)");
		} catch (IOException ex) {
			System.out.println("[Apollo] An error ocurred while saving the log file for " + player.getName() + "." + " (IOException_2)");
		}
		
	}

}
