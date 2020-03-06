package pro.dracarys.NoDisconnectSpam;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainCommand implements CommandExecutor, TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        List<String> lista = new ArrayList<>();
        if (args.length == 0) {
            lista.add("nds");
            lista.add("nodisconnectspam");
        }
        if (args.length == 1) {
            lista.addAll(StringUtil.copyPartialMatches(args[0], Collections.singletonList("reload"), new ArrayList<>()));
        }
        return lista;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            String arg1 = args[0];
            if (arg1.equalsIgnoreCase("reload")) {
                NoDisconnectSpam.getInstance().reloadConfig();
                sender.sendMessage(Util.color("&aConfig Reloaded!"));
            } else {
                sendUsage(sender);
            }
        } else {
            sendUsage(sender);
        }
        return true;
    }

    public void sendUsage(CommandSender sender) {
        sender.sendMessage(Util.color("&aNoDisconnectSpam &eby Valentina_pro"));
        sender.sendMessage(Util.color("&e/nds reload&7 >> &fReload Config"));
    }
}
