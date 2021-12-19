package me.teakivy.endreset;

import me.teakivy.endreset.Commands.ResetEndCommand;
import me.teakivy.endreset.Events.UpdateJoinAlert;
import me.teakivy.endreset.Utils.ConfigUpdater;
import me.teakivy.endreset.Utils.EndReset;
import me.teakivy.endreset.Utils.Metrics.Metrics;
import me.teakivy.endreset.Utils.UpdateChecker;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.logging.Logger;

import static me.teakivy.endreset.Utils.Metrics.CustomMetrics.registerCustomMetrics;

public final class Main extends JavaPlugin {
    public Boolean newVersionAvaliable = false;
    public String latestVTVersion;

    public static boolean devMode = false;
    public static Logger logger;

    @Override
    public void onEnable() {
        logger = getLogger();

        this.saveDefaultConfig();

        if (!getConfig().getBoolean("config.ignore-warnings")) {
            logger.info(ChatColor.YELLOW + "This plugin is still in development. Please report any bugs to the github or on our Discord Server");
            logger.info(ChatColor.YELLOW + "If you want to ignore this warning, please set \"config.ignore-warnings\" to true in the config.yml file");
            logger.info(ChatColor.YELLOW + "https://github.com/teakivy/end-reset/issues");
            logger.info(ChatColor.YELLOW + "https://dsc.gg/teakivy");
        }

        new ResetEndCommand().register();

        if (getConfig().getBoolean("config.dev-mode")) {
            devMode = true;
            getLogger().info("Dev mode enabled!");
        }

        if (!this.getConfig().getBoolean("config.dev-mode")) {
            if (this.getConfig().getInt("config.version") < Objects.requireNonNull(this.getConfig().getDefaults()).getInt("config.version")) {
                try {
                    ConfigUpdater.update(this, "config.yml", new File(this.getDataFolder(), "config.yml"), Collections.emptyList(), true);
                    this.reloadConfig();
                    this.getLogger().info("Config updated to version " + this.getConfig().getInt("config.version"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Main.devMode) logger.info("Updated Config");
            }
        } else {
            try {
                ConfigUpdater.update(this, "config.yml", new File(this.getDataFolder(), "config.yml"), Collections.emptyList(), true);
                this.reloadConfig();
                this.getLogger().info("Config updated to version " + this.getConfig().getInt("config.version"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Main.devMode) logger.info("Updated Config");
        }


        getServer().getPluginManager().registerEvents(new UpdateJoinAlert(), this);
        if (Main.devMode) logger.info("Registered update event");

        String latestVersion = null;
        try {
            latestVersion = new UpdateChecker(this, 98479).getLatestVersion();
            if (Main.devMode) logger.info("Found latest version");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            getLogger().severe(ChatColor.RED + "Failed to check for updates");
        }
        String thisVersion = this.getDescription().getVersion();
        if (!thisVersion.equalsIgnoreCase(latestVersion)) {
            getLogger().warning(ChatColor.GOLD + "A new version of EndReset is available: " + latestVersion);
            getLogger().warning(ChatColor.YELLOW + "https://www.spigotmc.org/resources/end-reset-1-18.98479/");
            newVersionAvaliable = true;
            latestVTVersion = latestVersion;
            if (Main.devMode) logger.info("Proposed new version " + latestVersion);
        }

        Metrics metrics = new Metrics(this, 13640);
        registerCustomMetrics(metrics);
        if (Main.devMode) logger.info("Registered Metrics");

        if (getConfig().getBoolean("reset-on-enable")) {
            logger.info("Resetting end");
            EndReset.resetEnd(null, getConfig().getString("world-folder-name"));
        }
    }

    @Override
    public void onDisable() {

    }
}
