package me.kansio.client.modules.impl.player;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.utils.chat.ChatUtil;
import net.minecraft.util.BlockPos;

import java.text.MessageFormat;

public class AntiVoid extends Module {
    public AntiVoid() {
        super("AntiVoid", ModuleCategory.PLAYER);
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        event.setPosY(mc.thePlayer.posY + mc.thePlayer.fallDistance);
    }

    public boolean isVoidBlock(double x, double y, double z) {
       return false;
    }

}
