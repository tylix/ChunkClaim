package me.tylix.chunkclaim.game.deathcause;

import org.bukkit.event.entity.EntityDamageEvent;

public class DeathCauseData {

    private final EntityDamageEvent.DamageCause damageCause;
    private final String[] message;

    public DeathCauseData(EntityDamageEvent.DamageCause damageCause, String[] message) {
        this.damageCause = damageCause;
        this.message = message;
    }

    public EntityDamageEvent.DamageCause getDamageCause() {
        return damageCause;
    }

    public String[] getMessage() {
        return message;
    }
}
