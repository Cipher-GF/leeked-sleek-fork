package me.kansio.client.utils.player;

import me.kansio.client.utils.Util;
import me.kansio.client.utils.chat.ChatUtil;

public class TimerUtil extends Util {

    public static void Timer(Float timer) {
        if (thePlayer.isServerWorld() && thePlayer.isEntityAlive()) {
            mc.timer.timerSpeed = timer.byteValue();
        } else {
            ChatUtil.log("tESt");
        }
    }

    public static void Timer(Float timer, Float ticks) {
        if (thePlayer.isServerWorld() && thePlayer.isEntityAlive()) {
            if (thePlayer.ticksExisted % ticks == 0) {
                mc.timer.timerSpeed = timer.byteValue();
            }
        } 
    }

    public static void Timer(Float timer, Float ticks, Boolean onGround) {
        if (thePlayer.isServerWorld() && thePlayer.isEntityAlive()) {
            if (thePlayer.onGround == onGround) {
                if (thePlayer.ticksExisted % ticks == 0) {
                    mc.timer.timerSpeed = timer.byteValue();
                }
            }
        } else {
            ChatUtil.log("tESt");
        }
    }
}
