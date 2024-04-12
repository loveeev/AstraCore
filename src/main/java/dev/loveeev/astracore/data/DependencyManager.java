package dev.loveeev.astracore.data;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public enum DependencyManager {
    ITEMS_LANG_API("ItemsLangApi", true),
    TOWNY("Towny", false),
    VAULT("Vault", false);

    private final String name;
    private final boolean isOptional;

    DependencyManager(String name, boolean optional) {
        this.name = name;
        this.isOptional = optional;
    }

    public final boolean isLoaded() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(this.name);
        return plugin != null && plugin.isEnabled();
    }

    /**
     * Print messages to console if some soft dependencies are not loaded or enabled.
     */
    public static void checkSoftDependencies(){
        List<String> soft = new ArrayList<>();
        for (DependencyManager dep : DependencyManager.values()){
            if (dep.isOptional && !dep.isLoaded()){
                soft.add(dep.name);
            }
        }

        if (!soft.isEmpty()){
            Bukkit.getLogger().warning("[SunTowny] The following soft dependencies are not loaded or enabled: "
                    + soft + ". Some features may not work.");
        }
    }
}
