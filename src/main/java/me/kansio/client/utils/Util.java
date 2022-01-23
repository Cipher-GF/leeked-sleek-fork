package me.kansio.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

public abstract class Util {

    protected static Minecraft mc = Minecraft.getMinecraft();

    protected static EntityPlayerSP thePlayer = mc.thePlayer;

    protected static WorldClient theWorld = mc.theWorld;
}
