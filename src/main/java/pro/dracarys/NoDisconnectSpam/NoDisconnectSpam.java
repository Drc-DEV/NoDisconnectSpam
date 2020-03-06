package pro.dracarys.NoDisconnectSpam;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import pro.dracarys.NoDisconnectSpam.hooks.CombatLogXHook;
import pro.dracarys.NoDisconnectSpam.hooks.HookManager;

import java.util.HashMap;
import java.util.Map;

public class NoDisconnectSpam extends JavaPlugin implements Listener {

    private static NoDisconnectSpam plugin;

    public static NoDisconnectSpam getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        PluginCommand cmd = this.getCommand("nds");
        MainCommand executor = new MainCommand();
        if (cmd != null) {
            cmd.setExecutor(executor);
            cmd.setTabCompleter(executor);
        }
        Bukkit.getPluginManager().registerEvents(this, this);
        printPluginInfo();
        HookManager.getInstance().loadHooks();
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        HookManager.getInstance().getEnabledHooks().clear();
        plugin = null;
    }

    private Map<Player, Event> inKickProcessPlayers = new HashMap<>();


    @EventHandler(priority = EventPriority.LOWEST)
    public void onEarlyKick(PlayerKickEvent e) {
        if (isDisconnectSpamKick(e)) {
            inKickProcessPlayers.put(e.getPlayer(), e);
            new BukkitRunnable() {
                public void run() {
                    inKickProcessPlayers.remove(e.getPlayer());
                }
            }.runTaskLaterAsynchronously(this, 200L);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKick(PlayerKickEvent e) {
        final Player p = e.getPlayer();
        boolean isDisconnectSpamKick = inKickProcessPlayers.containsKey(p);
        inKickProcessPlayers.remove(p);
        if (!isDisconnectSpamKick) {
            isDisconnectSpamKick = isDisconnectSpamKick(e);
        }
        if (isDisconnectSpamKick) {
            e.setCancelled(true);
            p.sendMessage(Util.color(getConfig().getString("Settings.replace-message")));
            if (getConfig().getBoolean("Settings.kill-spammer")) {
                if (getConfig().getBoolean("Settings.hooks.combatlogx.kill-only-if-in-combat") && CombatLogXHook.isInCombat(p)) {
                    p.damage(10000);
                } else {
                    p.damage(10000);
                }
            }
        }
    }


    private boolean isDisconnectSpamKick(PlayerKickEvent e) {
        boolean isDisconnectSpamKick = false;
        for (String disabledKick : getConfig().getStringList("Settings.cancel-kick-reasons")) {
            if (disabledKick.toLowerCase().contains(e.getReason().toLowerCase())) {
                isDisconnectSpamKick = true;
                break;
            }
        }
        return isDisconnectSpamKick;
    }

    // Tell IntelliJ to not format this, by enabling formatter markers in comments (Pref-> Editor-> Code Style)
    // Made this way for easy editing/char replacing, using equal size chars for all consoles compatibility.
    //@formatter:off
    private void printPluginInfo() {
        Util.sendConsole(("\n"+
                " ⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜\n" +
                " ⬜⬜⬛⬛⬛⬜⬜⬜⬜⬛⬛⬛⬜⬜⬜⬜⬛⬛⬛⬛⬜⬜\n" +
                " ⬜⬜⬛⬜⬜⬛⬜⬜⬜⬛⬜⬛⬛⬜⬜⬜⬛⬜⬜⬛⬜⬜\n" +
                " ⬜⬜⬛⬛⬛⬛⬜⬜⬜⬛⬜⬜⬛⬜⬜⬜⬛⬛⬜⬜⬜⬜\n" +
                " ⬜⬜⬛⬜⬜⬛⬜⬜⬜⬛⬜⬜⬛⬜⬜⬜⬜⬜⬛⬛⬜⬜\n" +
                " ⬜⬜⬛⬜⬜⬛⬜⬜⬜⬛⬜⬜⬛⬜⬜⬜⬛⬜⬜⬛⬜⬜\n" +
                " ⬜⬜⬛⬜⬜⬛⬛⬜⬜⬛⬛⬛⬜⬜⬜⬜⬛⬛⬛⬛⬜⬜\n" +
                " ⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜⬜\n"
        ).replace("⬜","&0█").replace("⬛","&f█") + "\n" +
                " &f-->  &c" + getDescription().getName() + " &7v" + getDescription().getVersion() + "&a Enabled" + "\n" +
                " &f-->  &f&o" + getDescription().getDescription() + "\n" +
                " &f-->  &eMade with &4♥ &eby &f" + getDescription().getAuthors().get(0) + "\n");
        if (getDescription().getVersion().contains("-DEV"))
            Util.sendConsole("&f&l[!] &cThis is a BETA, report any unexpected behaviour to the Author!"+ "\n");
    }
    //@formatter:on

}
