package me.kansio.client.modules.impl.visuals;

import me.kansio.client.Client;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import net.minecraft.client.entity.AbstractClientPlayer;

@ModuleData(
        name = "Cape",
        category = ModuleCategory.VISUALS,
        description = "Custom client capes"
)
public class Cape extends Module {

    public boolean canRender(AbstractClientPlayer player) {
        return player == mc.thePlayer || Client.getInstance().getFriendManager().isFriend(player.getName()) || Client.getInstance().getUsers().containsKey(player.getName());
    }

}
