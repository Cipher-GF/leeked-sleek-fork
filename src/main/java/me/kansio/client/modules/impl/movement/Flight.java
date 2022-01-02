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

public class Flight extends Module {

    private ModeValue modeValue = new ModeValue("Mode", this, "Vanilla", "Verus", "VerusDamage", "Funcraft", "Collide", "Ghostly", "Mush", "VerusGlide");
    private NumberValue<Double> speed = new NumberValue<>("Speed", this, 1d, 0d, 7d, 0.1);
    private BooleanValue viewbob = new BooleanValue("View Bobbing", this, true);
    private BooleanValue boost = new BooleanValue("Boost", this, true, modeValue, "Funcraft");
    private BooleanValue extraBoost = new BooleanValue("Extra Boost", this, true, modeValue, "Funcraft");
    private BooleanValue glide = new BooleanValue("Glide", this, true, modeValue, "Funcraft");
    private NumberValue<Double> timer = new NumberValue<>("Timer", this, 1d, 1d, 5d, 0.1, modeValue, "Mush");
    private boolean boosted = false;
    double speedy = 2.5;
    Stopwatch stopwatch = new Stopwatch();

    public float ticks = 0;
    private int level;
    private double moveSpeed, lastDist;
    private double spereeeedserz = 2.5;

    public Flight() {
        super("Flight", ModuleCategory.MOVEMENT);
        register(modeValue, speed, viewbob, boost, extraBoost, glide, timer);
    }


    public void onEnable() {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        boosted = false;
        level = 0;
        lastDist = 0.0D;
        spereeeedserz = 0.22;
        stopwatch.resetTime();

        if (modeValue.getValue() == "VerusDamage") {
            mc.timer.timerSpeed = 0.3f;
            PlayerUtil.damageVerus();
        }

        if (modeValue.getValueAsString().equalsIgnoreCase("funcraft") || modeValue.getValueAsString().equalsIgnoreCase("mush")) {
            mc.thePlayer.performHurtAnimation();
        }

        ticks = 0;

    }

    public void onDisable() {
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
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
            case "Mush": {
                if (mc.timer.timerSpeed > 1) {
                    mc.timer.timerSpeed -= 0.01;
                }

                if (speedy > 0.22) {
                    speedy -= 0.01;
                }
                break;
            }
            case "Vanilla": {
                double motionY = 0;

                if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    motionY = speed.getValue() / 2;
                }

                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    motionY = -(speed.getValue() / 2);
                }

                mc.thePlayer.motionY = motionY;
                PlayerUtil.setMotion(speed.getValue().floatValue());
                break;
            }
            case "VerusDamage": {
                if (mc.thePlayer.hurtResistantTime > 18) {
                    boosted = true;
                    spereeeedserz = speed.getValue() / 2;
                } else if (boosted && mc.thePlayer.hurtResistantTime < 2) {
                    spereeeedserz = 0.22f;
                } else if (boosted) {
                    spereeeedserz -= 0.01;
                }
                break;
            }
            case "Funcraft": {
                if (event.isPre()) {


                    if (glide.getValue()) {
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
            case "VerusDamage": {
                PlayerUtil.setMotion(event, spereeeedserz);
                break;
            }

            case "Ghostly":
                if (mc.thePlayer.ticksExisted % 3 == 0) {
                    mc.thePlayer.motionY = 0;
                    PlayerUtil.setMotion(speed.getValue().floatValue());
                } else {
                    PlayerUtil.setMotion(0.1f);
                }
            case "VerusGlide":
                if (mc.thePlayer.ticksExisted % 4 == 0) {
                    mc.thePlayer.motionY = 0.0f;
                    PlayerUtil.setMotion(0.4f);
                } else {
                    PlayerUtil.setMotion(0.1f);
                }
            case "Verus": {
                if (!mc.thePlayer.isInLava() && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && mc.thePlayer.ridingEntity == null && mc.thePlayer.hurtTime < 1) {
                    if (mc.thePlayer.isMoving()) {
                        mc.gameSettings.keyBindJump.pressed = false;
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.jump();
                            mc.thePlayer.motionY = 0.0;
                            PlayerUtil.strafe(speed.getValue().floatValue());
                            event.setMotionY(0.41999998688698);
                        }
                        PlayerUtil.strafe();
                    }
                }
                break;
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
            case "VerusGlide":
                if (event.getBlock() instanceof BlockAir) {
                    if (mc.thePlayer.isSneaking())
                        return;
                    double x = event.getX();
                    double y = event.getY();
                    double z = event.getZ();
                    if (y < mc.thePlayer.posY) {
                        if (mc.thePlayer.ticksExisted % 5 == 0) {
                            event.setAxisAlignedBB(AxisAlignedBB.fromBounds(-5, -1, -5, 5, 1.0F, 5).offset(x, y, z));
                        }
                    }
                }
                break;
            case "Mush":
            case "Collide":
            case "VerusDamage":
            case "Verus":
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

    private double getBaseMoveSpeed() {
        double n = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            n *= 1.0 + 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return n;
    }

    @Override
    public String getSuffix() {
        return " " + modeValue.getValueAsString();
    }
}
