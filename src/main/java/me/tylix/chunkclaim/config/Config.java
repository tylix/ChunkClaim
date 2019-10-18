package me.tylix.chunkclaim.config;

import com.google.common.collect.Lists;

public enum Config {

    MESSAGES("messages_en_US"),
    CHUNK_WORLD("world"),
    CHUNK_PRICE(500),
    CHUNK_CALCULATION("$chunk_price$ * ($chunk_size$ * 2)"),
    SPAWN_PROTECTION_RADIUS(200);

    private Object data;

    Config(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
