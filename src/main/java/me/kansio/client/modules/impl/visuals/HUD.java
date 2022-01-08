package me.kansio.client.modules.impl.visuals;

import dorkbox.messageBus.annotations.Subscribe;
import lombok.Getter;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.visuals.hud.HudMode;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.StringValue;
import me.kansio.client.utils.java.ReflectUtils;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class HUD extends Module {

    private final List<? extends HudMode> modes = ReflectUtils.getRelects(this.getClass().getPackage().getName() + ".hud", HudMode.class).stream()
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
    private HudMode currentMode = modes.stream().anyMatch(speedMode -> speedMode.getName().equalsIgnoreCase(mode.getValue())) ? modes.stream().filter(hudMode -> hudMode.getName().equalsIgnoreCase(mode.getValue())).findAny().get() : null ;

    public BooleanValue font = new BooleanValue("Font", this, false);
    public BooleanValue noti = new BooleanValue("Notifications", this, true);
    public BooleanValue bps = new BooleanValue("BPS", this, true);
    public BooleanValue importantOnly = new BooleanValue("Important Only", this, true);
    public StringValue clientName = new StringValue("Client Name", this, "Sleek");
    public StringValue listSuffix = new StringValue("Module Suffix", this, " [%s]");

    public static boolean notifications;

    public HUD() {
        super("HUD", ModuleCategory.VISUALS);
        register(mode, noti, font, importantOnly, clientName, listSuffix);
    }

    @Override
    public void onEnable() {
        currentMode = modes.stream().anyMatch(hudMode -> hudMode.getName().equalsIgnoreCase(mode.getValue())) ? modes.stream().filter(hudMode -> hudMode.getName().equalsIgnoreCase(mode.getValue())).findAny().get() : null ;
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
