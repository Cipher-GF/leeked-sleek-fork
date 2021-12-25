package me.kansio.client.modules.impl.movement;

import dorkbox.messageBus.annotations.Subscribe;
import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.utils.player.PlayerUtil;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {

    private ModeValue mode = new ModeValue("Mode", this, "Vanilla", "Verus", "Ghostly", "Ghostly Teleport");
    private NumberValue<Double> speed = new NumberValue<>("Speed", this, 0.5, 0.0, 8.0, 0.1);

    public Speed() {
        super("Speed", Keyboard.KEY_G, ModuleCategory.MOVEMENT);
        register(mode, speed);
    }

    @Override
    public void onDisable() {
        PlayerUtil.setMotion(0);
    }

    @Subscribe
    public void onMove(MoveEvent event) {
        switch (mode.getValueAsString()) {
            case "Ghostly": {
                if (mc.thePlayer.ticksExisted % 3 == 0) {
                    PlayerUtil.setMotion(speed.getValue().floatValue());
                } else {
                    PlayerUtil.setMotion(0.4f);
                }
                break;
            }
            case "Verus": {
                if (mc.thePlayer.onGround) {
                    event.setMotionY(mc.thePlayer.motionY = 0.42);
                }

                float sped2 = (float) (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.365 : 0.355);

                if (mc.thePlayer.hurtTime >= 1) {
                    sped2 = speed.getValue().floatValue();
                }

                PlayerUtil.setMotion(sped2);
                break;
            }
        }
    }


        @Subscribe
        public void onUpdate (UpdateEvent event){
            switch (mode.getValueAsString()) {
                case "Vanilla": {
                    PlayerUtil.setMotion(speed.getValue().floatValue());
                    break;
                }
                case "Ghostly Teleport": {
                    double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
                    double x = -Math.sin(yaw) * 1.8;
                    double z = Math.cos(yaw) * 1.8;

                    if (!mc.thePlayer.isMoving()) return;

                    if (mc.thePlayer.ticksExisted % 5 == 0) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                    }
                    break;
                }
            }
        }

        @Override
        public String getSuffix () {
            return " " + mode.getValueAsString();
        }
    }
