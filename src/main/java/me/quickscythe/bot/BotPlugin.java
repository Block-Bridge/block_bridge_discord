package me.quickscythe.bot;

public class BotPlugin {

    private final String NAME;


    public BotPlugin(String name){
        this.NAME = name;
    }

    public void enable(){
        System.out.println("Plugin enabled");
    }

    public String getName() {
        return NAME;
    }
}
