package me.tylix.chunkclaim.module.manager;

import me.tylix.chunkclaim.module.interfaces.IChunkModule;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class ModuleManager {

    private final File file = new File("ChunkClaim/modules");

    private final List<IChunkModule> modules;

    public ModuleManager() {
        this.modules = new ArrayList<>();
        if (!file.exists())
            file.mkdir();
    }

    public void loadModules() {
        System.out.println("Loading modules...");
        final File[] files = new File("ChunkClaim/modules").listFiles();
        if (files == null) return;
        for (File moduleFile : files) {
            try {
                this.loadFile(moduleFile.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("All modules (" + modules.size() + ") loaded.");
    }

    private void loadFile(String jar) throws Exception {
        final JarFile jarFile = new JarFile("ChunkClaim/modules/" + jar);
        final Manifest manifest = jarFile.getManifest();
        final Attributes attrib = manifest.getMainAttributes();
        final String main = attrib.getValue(Attributes.Name.MAIN_CLASS);
        final Class cl = new URLClassLoader(new URL[]{new File("ChunkClaim/modules/" + jar).toURL()}).loadClass(main);
        final Class[] interfaces = cl.getInterfaces();
        boolean isplugin = false;
        for (int y = 0; y < interfaces.length && !isplugin; y++)
            if (interfaces[y].getName().equals("me.tylix.chunkclaim.module.interfaces.IChunkModule"))
                isplugin = true;
        if (isplugin) {
            final IChunkModule module = (IChunkModule) cl.newInstance();
            this.modules.add(module);
        }
    }

    public List<IChunkModule> getModules() {
        return modules;
    }
}
