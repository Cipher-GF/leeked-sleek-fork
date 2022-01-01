package me.kansio.client.commands.impl;

import lombok.Getter;
import net.minecraft.client.Minecraft;

public abstract class Command {

    protected static final Minecraft mc = Minecraft.getMinecraft();

    @Getter
    private String name;

    public Command(String name) {
        this.name = name;
    }

    public abstract void run(String[] args);

}
