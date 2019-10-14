package me.tylix.chunkclaim.message;

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
