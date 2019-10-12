package me.tylix.chunkclaim.module.manager;

import me.tylix.chunkclaim.module.interfaces.IChunkModule;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final File file = new File("plugins/ChunkClaim/modules");

    private final List<IChunkModule> modules;

    public ModuleManager() {
        this.modules = new ArrayList<>();
        if (!file.exists())
            file.mkdir();
    }

    public void loadModules() {
        System.out.println("Loading modules...");
        for (Plugin plugin : Bukkit.getPluginManager().loadPlugins(file))
            Bukkit.getPluginManager().enablePlugin(plugin);
        System.out.println("All modules (" + modules.size() + ") loaded.");
    }

    public void registerModule(IChunkModule module) {
        modules.add(module);
    }

    public List<IChunkModule> getModules() {
        return modules;
    }
}
