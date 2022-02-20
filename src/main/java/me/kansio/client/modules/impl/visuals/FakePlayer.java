package me.kansio.client.modules.impl.visuals;


import com.mojang.authlib.GameProfile;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;
@ModuleData(
        name = "FakePlayer",
        category = ModuleCategory.VISUALS,
        description = "Shows a fake player in the world."
)
public class FakePlayer extends Module {

    @Override
    public void onEnable() {
        EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(FakePlayer.mc.theWorld, new GameProfile(UUID.fromString("4f7700aa-93d0-4c6a-b58a-d99b1c7287fd"), "qoft"));
        fakePlayer.copyLocationAndAnglesFrom(FakePlayer.mc.thePlayer);
        FakePlayer.mc.theWorld.addEntityToWorld(69420, fakePlayer);
    }

    @Override
    public void onDisable() {
        FakePlayer.mc.theWorld.removeEntityFromWorld(69420);
    }
}
