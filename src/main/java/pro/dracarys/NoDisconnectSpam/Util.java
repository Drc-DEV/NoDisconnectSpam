package pro.dracarys.NoDisconnectSpam;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Util {

    public static void sendConsole(String str) {
        Bukkit.getConsoleSender().sendMessage(color(str));
    }

    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}
