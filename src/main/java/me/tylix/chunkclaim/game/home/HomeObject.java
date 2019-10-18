package me.tylix.chunkclaim.game.home;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeObject {

    private final UUID uuid;
    private final List<HomeData> homes;

    public HomeObject(UUID uuid) {
        this.uuid = uuid;
        homes = new ArrayList<>();
    }
}
