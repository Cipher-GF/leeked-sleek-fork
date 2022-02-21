package me.kansio.client.modules.impl.movement;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.Client;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.value.value.BooleanValue;
import me.kansio.client.value.value.ModeValue;
import me.kansio.client.value.value.NumberValue;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import org.lwjgl.Sys;

import java.lang.Number;
@ModuleData(
        name = "Step",
        category = ModuleCategory.MOVEMENT,
        description = "Step over blocks."
)
public class Step extends Module {
    private ModeValue mode = new ModeValue("Mode", this, "Vanilla", "Verus", "Jump", "NCP");
    public BooleanValue cage_checks = new BooleanValue("Cage Checks", this, true);
    private NumberValue<Float> height = new NumberValue<>("Height", this, 1.5f, 1.0f, 6.0f, 0.1f);
    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.stepHeight = 0.625F;
    }
    @Subscribe
    public void UpdateEvent(UpdateEvent event) {
        switch (mode.getValue()) {
            case "Vanilla":
                System.out.println("Vanilla");
                if ((mc.thePlayer.isCollidedHorizontally) && (mc.thePlayer.onGround) && (!Client.getInstance().getModuleManager().getModuleByName("Flight").isToggled())) {
                    mc.thePlayer.stepHeight = height.getValue();
                } else {
                    mc.thePlayer.stepHeight = 0.6F;
                }
                break;
            case "Verus":
                if ((mc.thePlayer.isCollidedHorizontally) && (mc.thePlayer.onGround) && (!mc.thePlayer.isInsideOfMaterial(Material.water)) && (!mc.thePlayer.isInsideOfMaterial(Material.lava))) {
                    String bUp = String.valueOf(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 3, mc.thePlayer.posZ )));
                    // get facing direction
                    int dir = mc.thePlayer.getHorizontalFacing().getHorizontalIndex();
                    String bForward = String.valueOf(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + (dir == 0 ? 1 : dir == 1 ? -1 : dir == 2 ? -1 : dir == 3 ? -2 : 0), mc.thePlayer.posY, mc.thePlayer.posZ + (dir == 2 ? -1 : dir == 3 ? 1 : 0))));
                    String b2Blocks = String.valueOf(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + (dir == 1 ? -1 : dir == 3 ? 1: 0), mc.thePlayer.posY + 1, mc.thePlayer.posZ + (dir == 0 ? 1 : dir == 2 ? -1 : 0))));
                    String bRight = String.valueOf(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + 1, mc.thePlayer.posY, mc.thePlayer.posZ )));
                    String bLeft = String.valueOf(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX - 1, mc.thePlayer.posY, mc.thePlayer.posZ )));
                    if (cage_checks.getValue()) {
                        if (!bRight.contains("minecraft:glass") && !bLeft.contains("minecraft:glass") && !bUp.contains("minecraft:glass") && b2Blocks.contains("minecraft:air")) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ, mc.thePlayer.onGround));
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.75, mc.thePlayer.posZ, mc.thePlayer.onGround));
                            mc.thePlayer.stepHeight = 1F;
                        } else {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ, mc.thePlayer.onGround));
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.75, mc.thePlayer.posZ, mc.thePlayer.onGround));
                            mc.thePlayer.stepHeight = 1F;
                        }

                    } else {
                        mc.thePlayer.stepHeight = 0.6F;
                    }
                }
                break;
            case "Jump":
                System.out.println("Jump");
                if ((mc.thePlayer.isCollidedHorizontally) && (mc.thePlayer.onGround)) {
                    mc.thePlayer.isAirBorne = true;
                    mc.thePlayer.triggerAchievement(StatList.jumpStat);
                    mc.thePlayer.jump();
                }
                break;
            case "NCP":
                try {
                    if ((mc.thePlayer.isCollidedHorizontally) && (mc.thePlayer.onGround)) {
                        mc.thePlayer.stepHeight = height.getValue();
                        double rheight = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;
                        if (rheight >= 0.625) {
                            try {
                                block12:
                                {
                                    double y;
                                    double posZ;
                                    double posX;
                                    block11:
                                    {
                                        posX = mc.thePlayer.posX;
                                        posZ = mc.thePlayer.posZ;
                                        y = mc.thePlayer.posY;
                                        if (!(rheight < 1.1)) break block11;
                                        double first = 0.42;
                                        double second = 0.75;
                                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + first, posZ, false));
                                        if (!(y + second < y + rheight)) break block12;
                                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + second, posZ, false));
                                        break block12;
                                    }
                                    if (rheight < 1.6) {
                                        double[] offset;
                                        for (double off : offset = new double[]{0.42, 0.33, 0.24, 0.083, -0.078}) {
                                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y += off, posZ, false));
                                        }
                                    } else if (rheight < 2.1) {
                                        double[] heights;
                                        for (double off : heights = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869}) {
                                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
                                        }
                                    } else {
                                        double[] heights;
                                        for (double off : heights = new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907}) {
                                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    } else {
                        mc.thePlayer.stepHeight = 0.6f;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }


    }



}
