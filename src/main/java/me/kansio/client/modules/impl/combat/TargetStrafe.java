package me.kansio.client.modules.impl.combat;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.Client;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.NumberValue;

public class TargetStrafe extends Module {

    public static double dir = -1;

    public BooleanValue autoF5 = new BooleanValue("Auto F5", this, false);
    public BooleanValue jump = new BooleanValue("On Jump", this, false);
    public BooleanValue control = new BooleanValue("Controllable", this, false);
    public BooleanValue render = new BooleanValue("Render", this, false);
    public NumberValue radius = new NumberValue("Radius", this, 3d, 0.1d, 5d, 0.1, render);
    public NumberValue width = new NumberValue("Width", this, 1d, 0.1d, 5d, 0.1, render);

    public TargetStrafe() {
        super("Target Strafe", ModuleCategory.COMBAT);
        register(autoF5, jump, control, render, radius, width);
    }

    @Override
    public void onDisable() {
        dir = -1;
    }

    @Subscribe
    public void onMotion(UpdateEvent event) {
        if (autoF5.getValue() && Client.getInstance().getModuleManager().getModuleByName("KillAura").isToggled()) {
            if (KillAura.target == null) {
                mc.gameSettings.thirdPersonView = 0;
            } else {
                mc.gameSettings.thirdPersonView = 1;
            }
        }

        if (event.isPre()) {
            if (control.getValue()) {
                if (mc.gameSettings.keyBindLeft.isPressed()) {
                    dir = 1;
                } else if (mc.gameSettings.keyBindRight.isPressed()) {
                    dir = -1;
                }
            }

            if (mc.thePlayer.isCollidedHorizontally) {
                invertStrafe();
            }
        }
    }

    private void invertStrafe() {
        dir = -dir;
    }

    public boolean canStrafe() {
        if (!Client.getInstance().getModuleManager().getModuleByName("KillAura").isToggled()) {
            return false;
        }

        if (jump.getValue()) {
            if (mc.gameSettings.keyBindJump.isKeyDown())
                return KillAura.target != null;
            else
                return false;
        } else {
            return KillAura.target != null;
        }
    }
}
