package me.kansio.client.modules.impl.movement;

import com.google.common.eventbus.Subscribe;
import me.kansio.client.event.impl.MoveEvent;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.event.impl.UpdateEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.utils.math.Stopwatch;
import me.kansio.client.utils.player.PlayerUtil;
import me.kansio.client.utils.player.TimerUtil;
import me.kansio.client.utils.render.RenderUtil;
import me.kansio.client.value.value.ModeValue;
import me.kansio.client.value.value.NumberValue;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.text.DecimalFormat;

@ModuleData(
        name = "Long Jump",
        category = ModuleCategory.MOVEMENT,
        description = "Jump further than normal"
)
public class LongJump extends Module {

    //Verus Highjump Variables
    boolean launched = false;
    boolean wasLaunched = false;
    boolean jumped = false;
    private ModeValue mode = new ModeValue("Mode", this, "Verus", "Viper", "Vanilla", "Test");
    //verus boost stuff
    private NumberValue<Double> vertical = new NumberValue<>("Vertical Boost", this, 0.8, 0.05, 6.0, 0.1);
    private NumberValue<Double> boost = new NumberValue<>("Speed", this, 1.45, 0.05, 10.0, 0.1);
    private Stopwatch damageWaiterThing = new Stopwatch();

    @Override
    public void onEnable() {
        launched = false;
        wasLaunched = false;
        jumped = false;
        damageWaiterThing.resetTime();


        if ("Verus".equals(mode.getValue())) {
            if (!mc.thePlayer.onGround) {
                toggle();
                return;
            }
            TimerUtil.setTimer(0.3f);
            PlayerUtil.damageVerus();
        } else if ("Test".equals(mode.getValue())) {
            //PlayerUtil.damagePlayer(mc.thePlayer.onGround);
        }
    }

    @Override
    public void onDisable() {
        TimerUtil.Reset();
        jumped = false;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        switch (mode.getValue()) {
            case "Verus": {
                if (mc.thePlayer.hurtTime > 1 && !launched) {
                    launched = true;
                }
                if (launched) {
                    if (!jumped) {
                        mc.thePlayer.motionY = vertical.getValue().doubleValue();
                        jumped = true;
                    }
                    PlayerUtil.setMotion(boost.getValue().floatValue());
                    launched = false;
                    wasLaunched = true;
                    toggle();
                }
                break;
            }
            case "Vanilla": {
                if (mc.thePlayer.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = vertical.getValue().doubleValue();

                    }
                    PlayerUtil.setMotion(boost.getValue().floatValue());
                }
                break;
            }

        }
    }

    @Subscribe
    public void render(RenderOverlayEvent event) {
        if (mode.getValue().equals("Test")) {
            RenderUtil.drawBar((float) event.getSr().getScaledWidth_double() / 2, (float) (event.getSr().getScaledHeight_double() / 2) - 25, 100, 20, 1000, (float) damageWaiterThing.getTimeRemaining(1000), 0xFF00FF00);
            mc.fontRendererObj.drawStringWithShadow(new DecimalFormat("0.#").format(((double) (damageWaiterThing.getTimeRemaining(1000)) / 1000)), (float) event.getSr().getScaledWidth_double() / 2, (float) event.getSr().getScaledHeight_double() / 2, -1);
        }
    }

    @Subscribe
    public void onMove(MoveEvent event) {
        switch (mode.getValue()) {
            case "Viper": {
                if (!mc.thePlayer.onGround) return;
                TimerUtil.setTimer(0.3f);
                if (mc.thePlayer.isMoving()) {
                    for (int i = 0; i < 17; ++i) {
                        PlayerUtil.TPGROUND(event, 0.32, 0);
                    }
                }
                break;
            }

        }
    }

    @Override
    public String getSuffix() {
        return " " + mode.getValueAsString();
    }

}
