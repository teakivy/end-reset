package me.teakivy.endreset.Utils;

import me.teakivy.endreset.Main;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Objects;

public class EndReset {

    static Main main = Main.getPlugin(Main.class);

    public static void resetEnd(CommandSender sender, String worldName) {
        Bukkit.getWorld(worldName).save();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().getName().equals(worldName)) {
                Location spawn = player.getBedSpawnLocation();
                if (spawn == null) spawn = Objects.requireNonNull(Bukkit.getWorld(worldName)).getSpawnLocation();
                player.teleport(spawn);
            }
        }
        for (
        Chunk chunk : Bukkit.getWorld(worldName).getLoadedChunks()) {
            chunk.unload();
        }

        Bukkit.getServer().unloadWorld(worldName, true);

        File dir = new File(new File(Main.getPlugin(Main.class).getServer().getWorldContainer(), worldName).toString() + "/DIM1/region");

        for(File file1: dir.listFiles())
                if (!file1.isDirectory())
                if (!main.getConfig().getStringList(worldName).contains(file1.getName()))
                file1.delete();

        WorldCreator wcEnd = new WorldCreator(worldName);
        wcEnd.environment(World.Environment.THE_END);
        wcEnd.createWorld();

        sender.sendMessage(ChatColor.GREEN + "The End has been reset!");
    }
}
