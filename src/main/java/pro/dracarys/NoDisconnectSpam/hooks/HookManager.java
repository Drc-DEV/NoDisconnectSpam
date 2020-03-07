package pro.dracarys.NoDisconnectSpam.hooks;

import org.bukkit.Bukkit;
import pro.dracarys.NoDisconnectSpam.NoDisconnectSpam;
import pro.dracarys.NoDisconnectSpam.Util;

import java.util.HashSet;
import java.util.Set;

public class HookManager {

    private static HookManager hookManager;
    private NoDisconnectSpam plugin;

    private Set<String> enabledHooks;

    private HookManager(NoDisconnectSpam plugin) {
        enabledHooks = new HashSet<>();
        this.plugin = plugin;
    }

    public static HookManager getInstance() {
        if (hookManager == null) {
            hookManager = new HookManager(NoDisconnectSpam.getInstance());
        }
        return hookManager;
    }

    public void loadHooks() {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            // Load hooks with a delay, because plugins sometimes load before us even if
            // they are in the softdepend list, don't know why. This way we're 100% sure.

            if (checkHook("CombatLogX")) {
                CombatLogXHook.setup();
                if (CombatLogXHook.isSetup())
                    Bukkit.getPluginManager().registerEvents(new CombatLogXHook(), NoDisconnectSpam.getInstance());
            }

            if (!enabledHooks.isEmpty())
                Util.sendConsole("&e" + NoDisconnectSpam.getInstance().getName() + " Hooked to: &f" + enabledHooks.toString().replaceAll("\\[\\]", ""));

        }, 2);
    }

    private boolean checkHook(String pluginName) {
        if (Bukkit.getPluginManager().isPluginEnabled(pluginName)) {
            enabledHooks.add(pluginName);
            return true;
        }
        return false;
    }

    public Set<String> getEnabledHooks() {
        return enabledHooks;
    }

}
