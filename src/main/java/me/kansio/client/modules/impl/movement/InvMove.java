package me.kansio.client.modules.impl.movement;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

public class InvMove extends Module {

    private final KeyBinding[] keyBindings = new KeyBinding[]{
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindJump,
            mc.gameSettings.keyBindSprint
    };

    public InvMove() {
        super("InvMove", ModuleCategory.MOVEMENT);
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S2EPacketCloseWindow && (mc.currentScreen instanceof GuiInventory)) {
            event.setCancelled(true);
        }
        if (mc.currentScreen == null || mc.currentScreen instanceof GuiChat) return;

        for (KeyBinding keyBinding : keyBindings) {
            keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
        }
    }

}
