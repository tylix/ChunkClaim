package me.tylix.simplesurvival.game.item;

/*
 *
 * Class created on 14:48 - 15.03.2019
 * Package de.ifheroes.coreapi.bukkit.skullchanger
 *
 * @author Maximilian Wiegmann
 *
 * Copyright (c) 2016 - 2019 by Maximilian Wiegmann. All rights reserved.
 *
 */

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkullBuilder {

    private Class<?> skullMetaClass;
    private Class<?> tileEntityClass;
    private Class<?> blockPositionClass;

    private final ItemStack skull;
    private final SkullMeta skullMeta;
    private final List<String> lore = new ArrayList<>();

    private final String signature;
    private final String value;

    public SkullBuilder(String signature, String value) {
        this.signature = signature;
        this.value = value;
        this.skull = new ItemStack(Material.PLAYER_HEAD, 1);
        this.skullMeta = (SkullMeta) skull.getItemMeta();
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            this.skullMetaClass = Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftMetaSkull");
            tileEntityClass = Class.forName("net.minecraft.server." + version + ".TileEntitySkull");
            blockPositionClass = Class.forName("net.minecraft.server." + version + ".BlockPosition");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public SkullBuilder setDisplayname(String name) {
        this.skullMeta.setDisplayName(name);
        return this;
    }

    public SkullBuilder setAmount(int amount) {
        this.skull.setAmount(amount);
        return this;
    }

    public SkullBuilder addLoreLine(String line) {
        lore.add(line);
        return this;
    }

    public SkullBuilder addLoreArray(String[] lines) {
        for (int x = 0; x < lines.length; x++) {
            lore.add(lines[x]);
        }
        return this;
    }

    public SkullBuilder addLoreAll(List<String> lines) {
        lore.addAll(lines);
        return this;
    }

    public ItemStack build() {
        if (!lore.isEmpty()) {
            this.skullMeta.setLore(lore);
        }
        try {
            Field profileField = skullMetaClass.getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(this.skullMeta, this.getProfile(this.signature, this.value));
        } catch (Exception var9) {
            var9.printStackTrace();
        }
        this.skull.setItemMeta(this.skullMeta);
        return this.skull;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }

    private GameProfile getProfile(String signature, String value) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        Property property = new Property("textures", value, signature);
        profile.getProperties().put("textures", property);
        return profile;
    }

    public boolean setBlock(Block block) {
        if (block.getType() != Material.PLAYER_WALL_HEAD) {
            block.setType(Material.PLAYER_WALL_HEAD);
        }

        try {
            Object nmsWorld = block.getWorld().getClass().getMethod("getHandle").invoke(block.getWorld());
            Object tileEntity = null;
            Method getTileEntity = nmsWorld.getClass().getMethod("getTileEntity", blockPositionClass);
            tileEntity = tileEntityClass.cast(getTileEntity.invoke(nmsWorld, this.getBlockPositionFor(block.getX(), block.getY(), block.getZ())));
            tileEntityClass.getMethod("setGameProfile", GameProfile.class).invoke(tileEntity, this.getProfile(signature, value));
            return true;
        } catch (Exception var7) {
            var7.printStackTrace();
            return false;
        }
    }

    private Object getBlockPositionFor(int x, int y, int z) {
        Object blockPosition = null;

        try {
            Constructor<?> cons = blockPositionClass.getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE);
            blockPosition = cons.newInstance(x, y, z);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return blockPosition;
    }

}
