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
                System.out.println("Verus");
                if ((mc.thePlayer.isCollidedHorizontally) && (mc.thePlayer.onGround) && (!Step.mc.thePlayer.isInsideOfMaterial(Material.water)) && (!Step.mc.thePlayer.isInsideOfMaterial(Material.lava))) {
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
                                this.ncpStep(rheight);
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

    private void ncpStep(double rheight) {
        System.out.println("NCP  THING" + rheight);
    }


}
