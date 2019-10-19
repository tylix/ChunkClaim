package me.tylix.chunkclaim.game.home;

import me.tylix.chunkclaim.ChunkClaim;
import me.tylix.chunkclaim.game.chunk.location.ChunkLocation;
import me.tylix.chunkclaim.game.home.data.HomeData;
import me.tylix.chunkclaim.game.player.data.PlayerData;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public class HomeObject {

    private ChunkClaim instance = ChunkClaim.INSTANCE;

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
        for (HomeData homeData : homeDataList)
            if (homeData.getName().equals(name))
                homeDataList.remove(homeData);
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
