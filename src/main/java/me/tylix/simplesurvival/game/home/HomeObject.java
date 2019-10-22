package me.tylix.simplesurvival.game.home;

import me.tylix.simplesurvival.SimpleSurvival;
import me.tylix.simplesurvival.game.chunk.location.ChunkLocation;
import me.tylix.simplesurvival.game.home.data.HomeData;
import me.tylix.simplesurvival.game.player.data.PlayerData;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public class HomeObject {

    private SimpleSurvival instance = SimpleSurvival.INSTANCE;

    private final UUID uuid;

    public HomeObject(UUID uuid) {
        this.uuid = uuid;
    }

    public void addHome(final String name, final Location location) {
        final List<HomeData> homeDataList = this.getHomes();
        homeDataList.add(new HomeData(name, new ChunkLocation(location)));
        PlayerData data = instance.getChunkPlayer(uuid).getPlayerData();
        data.setHomes(homeDataList);
        instance.getChunkPlayer(uuid).updatePlayerData(data);
    }

    public void removeHome(final String name) {
        final List<HomeData> homeDataList = this.getHomes();
        homeDataList.remove(getHomeDataByName(name));
        PlayerData data = instance.getChunkPlayer(uuid).getPlayerData();
        data.setHomes(homeDataList);
        instance.getChunkPlayer(uuid).updatePlayerData(data);
    }

    public boolean existsHome(final String name) {
        for (HomeData home : this.getHomes())
            if (home.getName().equalsIgnoreCase(name))
                return true;
        return false;
    }

    public HomeData getHomeDataByName(final String name) {
        for (HomeData home : this.getHomes())
            if (home.getName().equalsIgnoreCase(name))
                return home;
        return null;
    }

    public List<HomeData> getHomes() {
        return instance.getChunkPlayer(uuid).getPlayerData().getHomes();
    }

}
