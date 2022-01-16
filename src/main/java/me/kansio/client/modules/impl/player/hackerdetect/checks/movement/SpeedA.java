package me.kansio.client.modules.impl.player.hackerdetect.checks.movement;

import me.kansio.client.Client;
import me.kansio.client.modules.impl.player.hackerdetect.HackerDetect;
import me.kansio.client.modules.impl.player.hackerdetect.checks.Check;
import me.kansio.client.utils.Util;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.math.BPSUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class SpeedA extends Check {

    @Override
    public String name() {
        return "Speed A";
    }

    @Override
    public void onUpdate() {
        if (Minecraft.getMinecraft().thePlayer.ticksExisted > 20) {

            for (EntityPlayer ent : Minecraft.getMinecraft().theWorld.playerEntities) {

                if (ent.ticksExisted < 20) continue;
                if (ent.fallDistance > 20) continue;

                if (ent.hurtTime != 0) continue;

                if (BPSUtil.getBPS(ent) > 30) {
                    HackerDetect.getInstance().getViolations().put(ent, HackerDetect.getInstance().getViolations().getOrDefault(ent, 1));

                    if (HackerDetect.getInstance().getViolations().get(ent) % 5 == 1) {
                        ChatUtil.logCheater(ent.getName() + " §7might be using §aSpeed (Check 1)");
                        if (!Client.getInstance().getTargetManager().isTarget(ent)) {
                            ChatUtil.logCheater(ent.getName() + " §7might be using §aSpeed (Check 1) §4§l(Flagged as cheater!)");
                            Client.getInstance().getTargetManager().getTarget().add(ent.getName());
                        }
                    }
                } else {
                    HackerDetect.getInstance().getViolations().put(ent, HackerDetect.getInstance().getViolations().getOrDefault(ent, 1));
                }
            }
        }
    }
}
