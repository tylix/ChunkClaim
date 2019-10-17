package me.tylix.chunkclaim.game.player.data;

import me.tylix.chunkclaim.game.chunk.data.ChunkData;

import java.util.List;

public class PlayerData {

    private int level;
    private int exp;
    private int maxExp;
    private int money;
    private int menuSlot;
    private String languageFile;
    private List<ChunkData> chunks;

    public PlayerData(int level, int exp, int maxExp, int money, int menuSlot, String languageFile, List<ChunkData> chunks) {
        this.level = level;
        this.exp = exp;
        this.maxExp = maxExp;
        this.money = money;
        this.menuSlot = menuSlot;
        this.languageFile = languageFile;
        this.chunks = chunks;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getMaxExp() {
        return maxExp;
    }

    public int getMoney() {
        return money;
    }

    public List<ChunkData> getChunks() {
        return chunks;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setMaxExp(int maxExp) {
        this.maxExp = maxExp;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setChunks(List<ChunkData> chunks) {
        this.chunks = chunks;
    }

    public String getLanguageFile() {
        return languageFile;
    }

    public void setLanguageFile(String languageFile) {
        this.languageFile = languageFile;
    }

    public int getMenuSlot() {
        return menuSlot;
    }

    public void setMenuSlot(int menuSlot) {
        this.menuSlot = menuSlot;
    }
}
