package me.kansio.client.modules.impl.visuals;

import dorkbox.messageBus.annotations.Subscribe;
import lombok.Getter;
import me.kansio.client.Client;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.visuals.hud.HudMode;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.StringValue;
import me.kansio.client.utils.java.ReflectUtils;
import me.kansio.client.utils.network.UserUtil;
import me.kansio.client.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;

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

        ScaledResolution scaledResolution = RenderUtils.getResolution();

        String text = "§7" + UserUtil.getBuildType(Integer.parseInt(Client.getInstance().getUid())) + " - §f" + Client.getInstance().getUid();
        int y = mc.ingameGUI.getChatGUI().getChatOpen() ? 12 : 0;


        mc.fontRendererObj.drawStringWithShadow(text, scaledResolution.getScaledWidth() - mc.fontRendererObj.getStringWidth(text) - 2, scaledResolution.getScaledHeight() - mc.fontRendererObj.FONT_HEIGHT - 2 - y, -1);
    }

}
