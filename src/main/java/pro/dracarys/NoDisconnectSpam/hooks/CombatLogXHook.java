package pro.dracarys.NoDisconnectSpam.hooks;

import com.SirBlobman.combatlogx.event.PlayerTagEvent;
import com.SirBlobman.combatlogx.utility.CombatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pro.dracarys.NoDisconnectSpam.NoDisconnectSpam;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatLogXHook implements Listener {

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

    public static void forcePunish(Player player) {
        if (!isSetup) return;
        try {
            CombatUtil.forcePunish(player);
        } catch (Exception ignored) {
            //Ignored
        }
    }

    public static boolean wasInCombat(Player player) {
        if (!lastCombatMap.containsKey(player.getUniqueId())) return false;
        return (System.currentTimeMillis() - lastCombatMap.get(player.getUniqueId()) <= NoDisconnectSpam.getInstance().getConfig().getInt("Settings.hooks.combatlogx.seconds-from-combat-start") * 1000L);
    }

    private static Map<UUID, Long> lastCombatMap = new HashMap<>();
    
    @EventHandler(ignoreCancelled = true)
    public void onCombatStart(PlayerTagEvent e) {
        lastCombatMap.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

}
