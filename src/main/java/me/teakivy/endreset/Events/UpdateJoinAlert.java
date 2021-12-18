package me.teakivy.endreset.Events;

import me.teakivy.endreset.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateJoinAlert implements Listener {

    Main main = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            if (main.getConfig().getBoolean("config.alert-op-on-new-version")) {
                if (main.newVersionAvaliable) {
                    player.sendMessage(ChatColor.GOLD + "A new version of EndReset is available: " + main.latestVTVersion + "\nhttps://www.spigotmc.org/resources/+-tweaks.94021/");
                }
            }
        }
    }

}
