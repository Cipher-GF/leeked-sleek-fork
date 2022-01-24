package me.kansio.client.utils.player;

import me.kansio.client.utils.Util;
import me.kansio.client.utils.chat.ChatUtil;

public class TimerUtil extends Util {

    public static void timer(Float timer) {
        if (mc.thePlayer.isServerWorld() && mc.thePlayer.isEntityAlive()) {
            mc.timer.timerSpeed = timer;
        }
    }

    public static void timer(Float timer, Float ticks) {
        if (mc.thePlayer.isServerWorld() && mc.thePlayer.isEntityAlive()) {
            if (mc.thePlayer.ticksExisted % ticks == 0) {
                mc.timer.timerSpeed = timer;
            }
        } 
    }

    public static void timer(Float timer, Float ticks, Boolean onGround) {
        if (mc.thePlayer.isServerWorld() && mc.thePlayer.isEntityAlive()) {
            if (mc.thePlayer.onGround == onGround) {
                if (mc.thePlayer.ticksExisted % ticks == 0) {
                    mc.timer.timerSpeed = timer;
                }
            }
        }
    }

    public static void timer(Float timer, Boolean onGround) {
        if (mc.thePlayer.isServerWorld() && mc.thePlayer.isEntityAlive()) {
            if (mc.thePlayer.onGround == onGround) {
                mc.timer.timerSpeed = timer;
            }
        }
    }
}
