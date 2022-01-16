package me.kansio.client.modules.impl.visuals;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.visuals.hud.HudMode;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.property.value.StringValue;
import me.kansio.client.utils.java.ReflectUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@ModuleData(
        name = "HUD",
        category = ModuleCategory.VISUALS,
        description = "The HUD... nothing special"
)
@Getter
public class HUD extends Module {

    private final List<? extends HudMode> modes = ReflectUtils.getReflects(this.getClass().getPackage().getName() + ".hud", HudMode.class).stream()
            .map(aClass -> {
                try {
                    return aClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            })
            .sorted(Comparator.comparing(hudMode -> hudMode != null ? hudMode.getName() : null))
            .collect(Collectors.toList());

    private final ModeValue mode = new ModeValue("Mode", this, modes.stream().map(HudMode::getName).collect(Collectors.toList()).toArray(new String[]{}));
    private HudMode currentMode = modes.stream().anyMatch(hudMode -> hudMode.getName().equalsIgnoreCase(mode.getValue())) ? modes.stream().filter(hudMode -> hudMode.getName().equalsIgnoreCase(mode.getValue())).findAny().get() : null ;

    private final ModeValue colorMode = new ModeValue("Color Mode", this, "Sleek", "Rainbow", "Astolfo", "Nitrogen");
    public BooleanValue font = new BooleanValue("Font", this, false);
    public BooleanValue noti = new BooleanValue("Notifications", this, true);
    public BooleanValue bps = new BooleanValue("BPS", this, true);
    public BooleanValue importantOnly = new BooleanValue("Important Only", this, true);
    public StringValue clientName = new StringValue("Client Name", this, "Sleek");
    public StringValue listSuffix = new StringValue("Module Suffix", this, " [%s]");

    public NumberValue arrayListY = new NumberValue("ArrayList Y", this, 4, 20, 20, 1);

    private final ModeValue scoreboardLocation = new ModeValue("Scoreboard", this, "Right", "Left");
    private final NumberValue<Double> scoreboardPos = new NumberValue<>("Scoreboard Y", this, 0.0, -500.0, 500.0, 1.0);

    public static boolean notifications;

    @Override
    public void onEnable() {
        currentMode = modes.stream().anyMatch(hudMode -> hudMode.getName().equalsIgnoreCase(mode.getValue())) ? modes.stream().filter(hudMode -> hudMode.getName().equalsIgnoreCase(mode.getValue())).findAny().get() : null ;
        assert currentMode != null;
        currentMode.onEnable();
    }

    @Override
    public void onDisable() {
        currentMode.onDisable();
    }

    @Subscribe
    public void onRenderOverlay(RenderOverlayEvent event) {
        currentMode.onRenderOverlay(event);
    }

}
