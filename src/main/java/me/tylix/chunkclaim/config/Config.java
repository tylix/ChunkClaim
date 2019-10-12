package me.tylix.chunkclaim.config;

public enum Config {

    MESSAGES("messages_DE_de"),
    CHUNK_PRICE(500),
    CHUNK_CALCULATION("$chunk_price$ * ($chunk_size$ * 2)");

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
