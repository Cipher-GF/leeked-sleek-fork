package today.sleek.client.modules.impl.movement;

import com.google.common.eventbus.Subscribe;
import today.sleek.base.event.impl.MoveEvent;
import today.sleek.base.modules.ModuleCategory;
import today.sleek.base.modules.ModuleData;
import today.sleek.client.modules.impl.Module;
import today.sleek.base.value.value.NumberValue;
import today.sleek.client.utils.player.PlayerUtil;

@ModuleData(
        name = "Custom Speed",
        description = "Gives the ability to customize speed in many ways",
        category = ModuleCategory.MOVEMENT
)
public class CustomSpeed extends Module {

    private NumberValue<Double> motionY = new NumberValue<>("Base MotionY", this, 0.42, 0.05, 5.0, 0.01);
    private NumberValue<Double> groundSpeed = new NumberValue<>("Onground speed", this, 0.4, 0d, 10.0, 0.01);

    @Subscribe
    public void onUpdate(MoveEvent event) {
        if (mc.thePlayer.isMovingOnGround()) {
            event.setMotionY(mc.thePlayer.motionY = PlayerUtil.getMotion(motionY.getValue().floatValue()));
            PlayerUtil.setMotion(event, groundSpeed.getValue());
        }
    }

}
