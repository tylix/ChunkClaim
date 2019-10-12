package me.tylix.chunkclaim.game.chunk;

import com.google.gson.Gson;
import me.tylix.chunkclaim.ChunkClaim;
import me.tylix.chunkclaim.config.Config;
import me.tylix.chunkclaim.game.chunk.data.ChunkData;
import me.tylix.chunkclaim.game.chunk.location.ChunkLocation;
import me.tylix.chunkclaim.game.player.data.PlayerData;
import me.tylix.chunkclaim.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Villager;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChunkObject {

    private ChunkClaim instance = ChunkClaim.INSTANCE;

    private final UUID uuid;

    public ChunkObject(UUID uuid) {
        this.uuid = uuid;
    }

    public void claimChunk(final Chunk chunk) throws ScriptException {
        int price = instance.getChunkPlayer(uuid).getNextChunkPrice();
        if (instance.getChunkPlayer(uuid).getPlayerData().getMoney() >= price) {
            Location minLocation = instance.getChunkManager().getMinLocation(chunk);
            Location maxLocation = instance.getChunkManager().getMaxLocation(chunk);
            ChunkLocation minChunkLocation = new ChunkLocation(minLocation);
            ChunkLocation maxChunkLocation = new ChunkLocation(maxLocation);
            String id = instance.generateKey(10);
            ChunkData chunkData = new ChunkData(id, uuid, new ArrayList<>(), maxChunkLocation, minChunkLocation);
            this.addChunk(chunkData);
            instance.getChunkPlayer(uuid).removeMoney(price);
            Bukkit.getPlayer(uuid).sendMessage(Message.SUCCESSFULLY_CLAIMED_CHUNK.getMessage(new DecimalFormat().format(price).replace(",", ".") ));
            instance.getChunkManager().getChunks().add(chunkData);
            instance.getChunkPlayer(uuid).setChunkSize(16 * instance.getChunkPlayer(uuid).getPlayerData().getChunks().size());
        } else {
            Bukkit.getPlayer(uuid).sendMessage(Message.NOT_ENOUGH_MONEY.getMessage(new DecimalFormat().format(price - instance.getChunkPlayer(uuid).getPlayerData().getMoney())).replace(",", "."));
        }

    }

    private void addChunk(final ChunkData chunkData) {
        final List<ChunkData> chunks = ChunkClaim.INSTANCE.getChunkPlayer(uuid).getPlayerData().getChunks();
        chunks.add(chunkData);
        final PlayerData playerData = instance.getChunkPlayer(uuid).getPlayerData();
        playerData.setChunks(chunks);
        instance.getChunkPlayer(uuid).updatePlayerData(playerData);
    }

}
