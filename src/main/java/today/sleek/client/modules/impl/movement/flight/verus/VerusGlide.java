package today.sleek.client.modules.impl.movement.flight.verus;

import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;
import today.sleek.base.event.impl.BlockCollisionEvent;
import today.sleek.base.event.impl.MoveEvent;
import today.sleek.client.modules.impl.movement.flight.FlightMode;
import today.sleek.client.utils.player.PlayerUtil;

public class VerusGlide extends FlightMode {
    public VerusGlide() {
        super("Verus Glide");
    }

    @Override
    public void onMove(MoveEvent event) {
        if (mc.thePlayer.ticksExisted % 4 == 0) {
            mc.thePlayer.motionY = 0.0f;
            PlayerUtil.setMotion(0.4f);
        } else {
            PlayerUtil.setMotion(0.1f);
        }
    }

    @Override
    public void onCollide(BlockCollisionEvent event) {
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
    }
}
