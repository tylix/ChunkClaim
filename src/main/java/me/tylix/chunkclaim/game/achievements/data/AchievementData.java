package me.tylix.chunkclaim.game.achievements.data;

import me.tylix.chunkclaim.message.Description;

public class AchievementData {

    private final String name;
    private final Description description;
    private final int moneyReward;

    public AchievementData(String name, Description description, int moneyReward) {
        this.name = name;
        this.description = description;
        this.moneyReward = moneyReward;
    }

    public String getName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public int getMoneyReward() {
        return moneyReward;
    }
}
