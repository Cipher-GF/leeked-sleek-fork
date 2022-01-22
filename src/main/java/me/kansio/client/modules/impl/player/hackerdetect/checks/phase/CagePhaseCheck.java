package me.kansio.client.modules.impl.player.hackerdetect.checks.phase;

import me.kansio.client.Client;
import me.kansio.client.modules.impl.player.hackerdetect.HackerDetect;
import me.kansio.client.modules.impl.player.hackerdetect.checks.Check;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.math.BPSUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class CagePhaseCheck extends Check {

    @Override
    public String name() {
        return "Phase (Type A)";
    }

    @Override
    public void onBlocksMCGameStartTick() {
        for (Entity ent : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            //make sure it's a player, if it's not, just return
            if (!(ent instanceof EntityPlayer)) {
                return;
            }

            if (ent.posY < Minecraft.getMinecraft().thePlayer.posY - 2) {
                if (!Client.getInstance().getTargetManager().isTarget((EntityPlayer) ent)) {
                    ChatUtil.logCheater(ent.getName() + " §7is using §aPhase (Check 1) §4§l(Flagged as cheater!)");
                    Client.getInstance().getTargetManager().getTarget().add(ent.getName());
                }
            }
        }
    }
}
