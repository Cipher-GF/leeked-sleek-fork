package me.kansio.client.utils.player;

import me.kansio.client.utils.Util;

public class TimerUtil extends Util {

    public static void Timer(Float timer) {
            mc.timer.timerSpeed = timer;
    }

    public static void Timer(Float timer, Float ticks) {
            if (thePlayer.ticksExisted % ticks == 0) {
                mc.timer.timerSpeed = timer;
            }
    }

    public static void Timer(Float timer, Float ticks, Boolean onGround) {
            if (thePlayer.onGround == onGround) {
                if (thePlayer.ticksExisted % ticks == 0) {
                    mc.timer.timerSpeed = timer;
                }
            }
    }
}
