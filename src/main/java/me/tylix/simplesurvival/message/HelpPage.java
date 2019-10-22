package me.tylix.simplesurvival.message;

import java.util.List;

public class HelpPage {

    private final List<String> help;

    public HelpPage(List<String> help) {
        this.help = help;
    }

    public List<String> getHelp() {
        return help;
    }
}
