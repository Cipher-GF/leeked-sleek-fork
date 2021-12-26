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
import me.kansio.client.utils.Stopwatch;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.math.MathUtil;
import me.kansio.client.utils.player.PlayerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;

import java.util.Locale;

public class Flight extends Module {

    private ModeValue modeValue = new ModeValue("Mode", this, "Vanilla", "Verus", "Funcraft", "Funcraft2");
    private NumberValue<Float> speed = new NumberValue<>("Speed", this, 1f, 0f, 7f, 0.1f);
    private BooleanValue viewbob = new BooleanValue("View Bobbing", this, true);
    private BooleanValue boost = new BooleanValue("Boost", this, true);
    private BooleanValue extraBoost = new BooleanValue("Extra Boost", this, true);
    private boolean boosted = false;
    double speedy = 2.5;
    Stopwatch stopwatch = new Stopwatch();

    public float ticks = 0;
    private int level;
    private double moveSpeed, lastDist;
    private double spereeeedserz = 2.5;

    public Flight() {
        super("Flight", Keyboard.KEY_F, ModuleCategory.MOVEMENT);
        register(modeValue, speed, viewbob, boost, extraBoost);
    }


    public void onEnable() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        boosted = false;
        level = 0;
        lastDist = 0.0D;
        stopwatch.resetTime();

        if (modeValue.getValueAsString().toLowerCase(Locale.ROOT) == "funcraft") {
            mc.thePlayer.performHurtAnimation();
        }

        ticks = 0;
        if (modeValue.getValueAsString().equals("Verus")) {
            spereeeedserz = 0.22;
            PlayerUtil.damageVerus();
        }
    }

    public void onDisable() {
        lastDist = 0.0D;
        boosted = false;
        mc.timer.timerSpeed = 1f;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (!event.isPre()) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            lastDist = Math.sqrt((xDist * xDist) + (zDist * zDist));
        }
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
                    System.out.println("snek");
                    motionY = -0.4;
                }

                mc.thePlayer.motionY = motionY;
                PlayerUtil.setMotion(speed.getValue());
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
            case "Funcraft2":
            case "Funcraft": {
                if (event.isPre()) {



                    if (modeValue.getValue() == "Funcraft2") {
                        if (mc.thePlayer.onGround) {
                            event.setPosY(mc.thePlayer.motionY = 0.5);
                        } else {
                            mc.thePlayer.motionY = -0.01;
                        }
                    } else {
                        mc.thePlayer.motionY = 0;
                    }


                    double result = 0.00000000334947 + MathUtil.getRandomInRange(0.00000000014947, 0.00000000064947);

                    if (mc.thePlayer.ticksExisted % 3 == 0) {
                        event.setPosY(mc.thePlayer.posY + result);
                        event.setOnGround(false);
                    }

                    if (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F && mc.thePlayer.onGround) {
                        if (!boost.getValue()) {
                            final float motionY = 0.42f + (mc.thePlayer.isPotionActive(Potion.jump) ? ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F) : 0);
                        }


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
            case "Funcraft2":
            case "Funcraft": {
                if (extraBoost.getValue() && boost.getValue()) {
                    if (!stopwatch.timeElapsed(135) && stopwatch.timeElapsed(20)) {
                        mc.timer.timerSpeed = 3.5f;
                    } else {
                        mc.timer.timerSpeed = 1f;
                    }
                    if (mc.thePlayer.hurtResistantTime == 19 && level < 20) {
                        //mc.timer.timerSpeed = 3.6f;
                        stopwatch.resetTime();
                    }
                }

                if (boost.getValue()) {
                    final float motionY = 0.42f + (mc.thePlayer.isPotionActive(Potion.jump) ? ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F) : 0);
                    if (mc.thePlayer.isMoving()) {
                        if (level != 1) {
                            if (level == 2) {
                                //mc.timer.timerSpeed = Math.max(1, 3.5F);
                                ++level;
                                moveSpeed *= mc.thePlayer.isPotionActive(Potion.moveSpeed) ? speed.getValue() - 0.3 : speed.getValue();
                                //Printer.print("2: "+moveSpeed);
                            } else if (level == 3) {
                                ++level;
                                double difference = (0.1D) * (lastDist - getBaseMoveSpeed());
                                moveSpeed = lastDist - difference;
                            } else {
                                level++;
                                if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) {
                                    level = 1;
                                }
                                moveSpeed = lastDist - lastDist / 159D;
                            }
                        } else {
                            ChatUtil.log("sad");
                            event.setMotionY(mc.thePlayer.motionY = motionY);
                            level = 2;
                            double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.5 : 1.62;
                            moveSpeed = boost * getBaseMoveSpeed() - 0.01D;
                        }
                    } else {
                        moveSpeed = 0;
                    }
                    //Printer.print("f: "+moveSpeed + " " + level);
                    moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                    PlayerUtil.setMotion(event, moveSpeed);
                    break;
                } else {
                    PlayerUtil.setMotion(event, getBaseMoveSpeed());
                }
                break;
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

    private double getBaseMoveSpeed() {
        double n = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            n *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return n;
    }


}
