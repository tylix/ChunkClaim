package me.tylix.simplesurvival.message;

public class Description {

    private final String[] description;

    public Description(String... description) {
        this.description = description;
    }

    public String[] getDescription() {
        return description;
    }
}
