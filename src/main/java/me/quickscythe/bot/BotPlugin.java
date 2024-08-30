package me.quickscythe.bot;

public class BotPlugin {

    private String name = null;


    public void enable(){
        System.out.println(name + " enabled");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
