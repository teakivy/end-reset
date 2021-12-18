package me.teakivy.endreset.Utils.Metrics;

import me.teakivy.endreset.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class CustomMetrics {

    static Main main = Main.getPlugin(Main.class);

    public static void registerCustomMetrics(Metrics metrics) {
        registerPackMetrics(metrics);
    }

    public static void registerPackMetrics(Metrics metrics) {
        metrics.addCustomChart(new Metrics.AdvancedPie("world_name", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() throws Exception {
                Map<String, Integer> valueMap = new HashMap<>();
                valueMap.put(main.getConfig().getString("world-folder-name"), 1);
                return valueMap;
            }
        }));

        metrics.addCustomChart(new Metrics.AdvancedPie("reset_on_enable", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() throws Exception {
                Map<String, Integer> valueMap = new HashMap<>();
                valueMap.put(main.getConfig().getString("reset-on-enable"), 1);
                return valueMap;
            }
        }));

        metrics.addCustomChart(new Metrics.AdvancedPie("regions_to_save", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() throws Exception {
                Map<String, Integer> valueMap = new HashMap<>();

                for (String region : main.getConfig().getStringList("regions-to-save")) {
                    valueMap.put(region, 1);
                }
                return valueMap;
            }
        }));
    }
}
