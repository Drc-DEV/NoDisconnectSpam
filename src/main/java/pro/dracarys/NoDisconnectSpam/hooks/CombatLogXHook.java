package pro.dracarys.NoDisconnectSpam.hooks;

import com.SirBlobman.combatlogx.utility.CombatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CombatLogXHook {

    private static boolean isSetup = false;

    public static boolean isSetup() {
        return isSetup;
    }

    public static void setup() {
        if (Bukkit.getServer().getPluginManager().getPlugin("CombatLogX") != null) isSetup = true;
    }

    public static boolean isInCombat(Player player) {
        if (!isSetup) return false;
        try {
            return CombatUtil.isInCombat(player);
        } catch (Exception e) {
            return false;
        }
    }

}
