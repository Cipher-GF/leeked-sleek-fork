package me.kansio.client.modules.impl.movement;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.BlockCollisionEvent;
import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

public class Flight extends Module {

    private ModeValue modeValue = new ModeValue("Mode", this, "Vanilla", "Verus");
    private NumberValue speed = new NumberValue("Speed", this, 1, 0, 7, 0.1, false);
    private BooleanValue viewbob = new BooleanValue("View Bobbing", this, true);
    private boolean boosted = false;
    private double spereeeedserz = 2.5;

    public Flight() {
        super("Flight", Keyboard.KEY_F, ModuleCategory.MOVEMENT);
        register(modeValue, speed, viewbob);
    }


    public void onEnable() {
        if (modeValue.getValueAsString().equals("Verus")) {
            spereeeedserz = 0.22;
            PlayerUtil.damageVerus();
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (viewbob.getValue() && mc.thePlayer.isMoving()) {
            mc.thePlayer.cameraYaw = 0.035f;
        } else {
            mc.thePlayer.cameraYaw = 0;
        }
        switch (modeValue.getValueAsString()) {
            case "Vanilla": {
                double motionY = 0;

                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    motionY = 0.4;
                }

                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    motionY = -0.4;
                }

                mc.thePlayer.motionY = motionY;
                PlayerUtil.setMotion(speed.getValue().floatValue());
                break;
            }
            case "Verus": {
                if (mc.thePlayer.hurtTime == 9) {
                    boosted = true;
                }
                if (boosted) {
                    if (mc.thePlayer.hurtTime > 8) {
                        spereeeedserz = speed.getValue();
                    } else if (mc.thePlayer.hurtResistantTime == 0) {
                        spereeeedserz = 0.22;
                    }
                }
                break;
            }
        }
    }

    @Subscribe
    public void onMove(MoveEvent event) {
        switch (modeValue.getValueAsString()) {
            case "Verus": {
                if (!boosted) {
                    PlayerUtil.setMotion(0);
                } else {
                    PlayerUtil.setMotion(event, spereeeedserz);
                }
            }
        }
    }

    @Subscribe
    public void onCollide(BlockCollisionEvent event) {

        switch (modeValue.getValueAsString()) {
            case "Verus": {
                if (event.getBlock() instanceof BlockAir) {
                    if (mc.thePlayer.isSneaking())
                        return;
                    double x = event.getX();
                    double y = event.getY();
                    double z = event.getZ();
                    if (y < mc.thePlayer.posY) {
                        event.setAxisAlignedBB(AxisAlignedBB.fromBounds(-5, -1, -5, 5, 1.0F, 5).offset(x, y, z));
                    }
                }
                break;
            }
         }
    }

}
