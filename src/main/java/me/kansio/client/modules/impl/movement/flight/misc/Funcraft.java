package me.kansio.client.modules.impl.movement.flight.misc;

import me.kansio.client.event.impl.BlockCollisionEvent;
import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.PacketEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.impl.movement.flight.FlightMode;
import me.kansio.client.utils.math.MathUtil;
import me.kansio.client.utils.math.Stopwatch;
import me.kansio.client.utils.player.PlayerUtil;
import net.minecraft.potion.Potion;

public class Funcraft extends FlightMode {

    private double moveSpeed, lastDist, ascension;
    private int level, wait, xd, xdnewtest;
    private Stopwatch timer = new Stopwatch();

    public Funcraft() {
        super("Funcraft");
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        if (!event.isPre()) {
            double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
            double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
            lastDist = Math.sqrt((xDist * xDist) + (zDist * zDist));
        }

        if (event.isPre()) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.15, mc.thePlayer.posZ);
                mc.thePlayer.motionY = 0.15;
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.15, mc.thePlayer.posZ);
                mc.thePlayer.motionY = -0.15;
            } else mc.thePlayer.motionY = 0;
            if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
                event.setOnGround(true);
            }
            double result = 0.00000000334947 + MathUtil.getRandomInRange(0.00000000014947, 0.00000000064947);
            if (mc.thePlayer.ticksExisted % 3 == 0) {
                event.setPosY(mc.thePlayer.posY + result);
                event.setOnGround(false);
            }
                        /*if (mc.thePlayer.ticksExisted % 3 == 0) {
                            ascension += value;
                        }
                        if (ascension > value * 7) {
                            event.setOnGround(false);
                            event.setY(mc.thePlayer.posY + ascension);
                            ascension /= 1.125F;
                        }*/
            // Printer.print(""+event.getY());
            if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.onGround) {
                if (!getFlight().getBoost().getValue()) {
                    final float motionY = 0.42f + (mc.thePlayer.isPotionActive(Potion.jump) ? ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F) : 0);
                    //mc.thePlayer.motionY = motionY;
                    //   mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + motionY, mc.thePlayer.posZ);
                }
                if ((getFlight().getBoostMode().getValue().equals("Damage") || getFlight().getBoostMode().getValue().equals("WOWOMG")) && getFlight().getBoost().getValue())
                    if (xd == 0) {
                       PlayerUtil.damagePlayer();
                        xd = 1;
                    }
            }
        }
    }

    @Override
    public void onMove(MoveEvent event) {
        if (getFlight().getExtraBoost().getValue() && getFlight().getBoost().getValue()) {
            if (!timer.timeElapsed(135) && timer.timeElapsed(20)) {
                mc.timer.timerSpeed = 3.5f;
            } else {
                mc.timer.timerSpeed = 1.0f;
            }
            if (level < 20) {
                //TimerUtil.Timer(3.6f);
                timer.resetTime();
            }
        }
        if (getFlight().getBoost().getValue()) {
            final float motionY = 0.42f + (mc.thePlayer.isPotionActive(Potion.jump) ? ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F) : 0);
            switch (getFlight().getBoostMode().getValue()) {
                case "Normal":
                case "Damage":
                    if (mc.thePlayer.isMoving()) {
                        if (level != 1) {
                            if (level == 2) {
                                //mc.timer.timerSpeed = Math.max(1, 3.5F);
                                ++level;
                                moveSpeed *= mc.thePlayer.isPotionActive(Potion.moveSpeed) ? getFlight().getSpeed().getValue() - 0.3 : getFlight().getSpeed().getValue();
                                //Printer.print("2: "+moveSpeed);
                            } else if (level == 3) {
                                ++level;
                                double difference = 0.1 * (lastDist - getBaseMoveSpeed());
                                moveSpeed = lastDist - difference;
                            } else {
                                level++;
                                if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) {
                                    level = 1;
                                }
                                moveSpeed = lastDist - lastDist / 159D;
                            }
                        } else if (mc.thePlayer.hurtResistantTime == 19 || getFlight().getBoostMode().getValue().equals("Normal")) {
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
                    if (level == 1 && mc.thePlayer.hurtResistantTime != 19 && !getFlight().getBoostMode().getValue().equals("Normal")) moveSpeed = 0.011;
                    PlayerUtil.setMotion(event, moveSpeed);
                    break;
                case "WOWOMG":
                    if (mc.thePlayer.isMoving()) {
                        if (level != 1) {
                            if (level == 2) {
                                //mc.timer.timerSpeed = Math.max(1, 3.5F);
                                ++level;
                                moveSpeed *= mc.thePlayer.isPotionActive(Potion.moveSpeed) ? getFlight().getSpeed().getValue() - 0.3 : getFlight().getSpeed().getValue();
                                //Printer.print("2: "+moveSpeed);
                            } else if (level == 3) {
                                ++level;
                                double difference = 0.01 * (lastDist - getBaseMoveSpeed());
                                moveSpeed = lastDist - difference;
                            } else {
                                level++;
                                if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) {
                                    level = 1;
                                }
                                moveSpeed = moveSpeed - moveSpeed / 159.9D;
                            }
                        } else {
                            event.setMotionY(mc.thePlayer.motionY = motionY);
                            level = 2;
                            double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.55 : 1.62;
                            moveSpeed = boost * getBaseMoveSpeed() - 0.01D;
                        }
                    } else {
                        moveSpeed = 0;
                    }
                    //  Printer.print("f: "+moveSpeed + " " + level);
                    moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
                    PlayerUtil.setMotion(event, moveSpeed);
                    break;
            }
        } else {
            PlayerUtil.setMotion(event, getBaseMoveSpeed());
            //TimerUtil.Timer(0.5f);
            //MoveUtil.TP(event, 0.28, 0);
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
    public void onPacket(PacketEvent event) {
        super.onPacket(event);
    }

    @Override
    public void onCollide(BlockCollisionEvent event) {
        super.onCollide(event);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        lastDist = 0.0D;
        xdnewtest = 0;
        ascension = 0;
        xd = 0;
    }
}
