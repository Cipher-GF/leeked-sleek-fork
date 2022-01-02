package me.kansio.client.utils.math;

import me.kansio.client.utils.Util;

public class BPSUtil extends Util {

    public static double getBPS() {
        return mc.thePlayer.getDistance(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ) * (mc.timer.ticksPerSecond * mc.timer.timerSpeed);
    }

}
