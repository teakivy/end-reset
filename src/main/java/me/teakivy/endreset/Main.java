package me.teakivy.endreset;

import me.teakivy.endreset.Commands.ResetEndCommand;
import me.teakivy.endreset.Events.UpdateJoinAlert;
import me.teakivy.endreset.Utils.ConfigUpdater;
import me.teakivy.endreset.Utils.Metrics.Metrics;
import me.teakivy.endreset.Utils.UpdateChecker;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import static me.teakivy.endreset.Utils.Metrics.CustomMetrics.registerCustomMetrics;

public final class Main extends JavaPlugin {
    public Boolean newVersionAvaliable = false;
    public String latestVTVersion;

    @Override
    public void onEnable() {
        new ResetEndCommand().register();
//        saveDefaultConfig();
        if (!this.getConfig().getBoolean("config.dev-mode")) {
            if (this.getConfig().getInt("config.version") < Objects.requireNonNull(this.getConfig().getDefaults()).getInt("config.version")) {
                try {
                    ConfigUpdater.update(this, "config.yml", new File(this.getDataFolder(), "config.yml"), Collections.emptyList(), true);
                    this.reloadConfig();
                    this.getLogger().info("Config updated to version " + this.getConfig().getInt("config.version"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                ConfigUpdater.update(this, "config.yml", new File(this.getDataFolder(), "config.yml"), Collections.emptyList(), true);
                this.reloadConfig();
                this.getLogger().info("Config updated to version " + this.getConfig().getInt("config.version"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        getServer().getPluginManager().registerEvents(new UpdateJoinAlert(), this);

        String latestVersion = null;
        try {
            latestVersion = new UpdateChecker(this, 94021).getLatestVersion();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            getLogger().severe(ChatColor.RED + "Failed to check for updates");
        }
        String thisVersion = this.getDescription().getVersion();
        if (!thisVersion.equalsIgnoreCase(latestVersion)) {
            getLogger().warning(ChatColor.GOLD + "A new version of EndReset is available: " + latestVersion);
            getLogger().warning(ChatColor.YELLOW + "https://www.spigotmc.org/resources/+-tweaks.94021/");
            newVersionAvaliable = true;
            latestVTVersion = latestVersion;
        }

        Metrics metrics = new Metrics(this, 13640);
        registerCustomMetrics(metrics);
    }

    @Override
    public void onDisable() {

    }
}
