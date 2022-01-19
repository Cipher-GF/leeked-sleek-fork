package me.kansio.client.modules.impl.visuals;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.api.ModuleData;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.visuals.hud.ArrayListMode;
import me.kansio.client.modules.impl.visuals.hud.InfoMode;
import me.kansio.client.modules.impl.visuals.hud.WaterMarkMode;
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

    // WaterMark Mode
    private final List<? extends WaterMarkMode> watermarkmodes = ReflectUtils.getReflects(this.getClass().getPackage().getName() + ".hud", WaterMarkMode.class).stream()
            .map(aClass -> {
                try {
                    return aClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            })
            .sorted(Comparator.comparing(watermarkMode -> watermarkMode != null ? watermarkMode.getName() : null))
            .collect(Collectors.toList());

    private final ModeValue watermarkmode = new ModeValue("Watermark Mode", this, watermarkmodes.stream().map(WaterMarkMode::getName).collect(Collectors.toList()).toArray(new String[]{}));
    private WaterMarkMode currentwatermarkmode = watermarkmodes.stream().anyMatch(watermarkMode -> watermarkMode.getName().equalsIgnoreCase(watermarkmode.getValue())) ? watermarkmodes.stream().filter(watermarkMode -> watermarkMode.getName().equalsIgnoreCase(watermarkmode.getValue())).findAny().get() : null ;

    // ArrayList Mode
    private final List<? extends ArrayListMode> arraylistmodes = ReflectUtils.getReflects(this.getClass().getPackage().getName() + ".hud", ArrayListMode.class).stream()
            .map(aClass -> {
                try {
                    return aClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            })
            .sorted(Comparator.comparing(arraylistMode -> arraylistMode != null ? arraylistMode.getName() : null))
            .collect(Collectors.toList());

    private final ModeValue arraylistmode = new ModeValue("Arraylist Mode", this, arraylistmodes.stream().map(ArrayListMode::getName).collect(Collectors.toList()).toArray(new String[]{}));
    private ArrayListMode currentarraylistmode = arraylistmodes.stream().anyMatch(arraylistMode -> arraylistMode.getName().equalsIgnoreCase(arraylistmode.getValue())) ? arraylistmodes.stream().filter(arraylistMode -> arraylistMode.getName().equalsIgnoreCase(arraylistmode.getValue())).findAny().get() : null ;

    // Info Mode
    private final List<? extends InfoMode> infomodes = ReflectUtils.getReflects(this.getClass().getPackage().getName() + ".hud", InfoMode.class).stream()
            .map(aClass -> {
                try {
                    return aClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            })
            .sorted(Comparator.comparing(infoMode -> infoMode != null ? infoMode.getName() : null))
            .collect(Collectors.toList());

    private final ModeValue infomode = new ModeValue("Info Mode", this, infomodes.stream().map(InfoMode::getName).collect(Collectors.toList()).toArray(new String[]{}));
    private InfoMode currentinfomode = infomodes.stream().anyMatch(infoMode -> infoMode.getName().equalsIgnoreCase(infomode.getValue())) ? infomodes.stream().filter(infoMode -> infoMode.getName().equalsIgnoreCase(infomode.getValue())).findAny().get() : null ;

    private final ModeValue colorMode = new ModeValue("Color Mode", this, "Sleek", "Rainbow", "Astolfo", "Nitrogen");
    public BooleanValue font = new BooleanValue("Font", this, false);
    public BooleanValue noti = new BooleanValue("Notifications", this, true);
    public BooleanValue hideRender = new BooleanValue("Hide Render", this, true);
    public BooleanValue bps = new BooleanValue("BPS", this, true);

    public BooleanValue watermark = new BooleanValue("Watermark", this, true);

    public StringValue clientName = new StringValue("Client Name", this, "Sleek");
    public StringValue listSuffix = new StringValue("Module Suffix", this, " [%s]");

    public NumberValue arrayListY = new NumberValue("ArrayList Y", this, 4, 0, 20, 1);

    private final ModeValue scoreboardLocation = new ModeValue("Scoreboard", this, "Right", "Left");
    private final NumberValue<Double> scoreboardPos = new NumberValue<>("Scoreboard Y", this, 0.0, -500.0, 500.0, 1.0);

    public static boolean notifications;

    @Override
    public void onEnable() {
        currentwatermarkmode = watermarkmodes.stream().anyMatch(watermarkMode -> watermarkMode.getName().equalsIgnoreCase(watermarkmode.getValue())) ? watermarkmodes.stream().filter(watermarkMode -> watermarkMode.getName().equalsIgnoreCase(watermarkmode.getValue())).findAny().get() : null ;
        currentarraylistmode = arraylistmodes.stream().anyMatch(arraylistMode -> arraylistMode.getName().equalsIgnoreCase(arraylistmode.getValue())) ? arraylistmodes.stream().filter(arraylistMode -> arraylistMode.getName().equalsIgnoreCase(arraylistmode.getValue())).findAny().get() : null ;
        currentinfomode = infomodes.stream().anyMatch(arraylistMode -> arraylistMode.getName().equalsIgnoreCase(infomode.getValue())) ? infomodes.stream().filter(infoMode -> infoMode.getName().equalsIgnoreCase(infomode.getValue())).findAny().get() : null ;
        currentwatermarkmode.onEnable();
        currentarraylistmode.onEnable();
        currentinfomode.onEnable();
    }

    @Override
    public void onDisable() {
        currentwatermarkmode.onDisable();
        currentarraylistmode.onDisable();
        currentinfomode.onDisable();
    }

    @Subscribe
    public void onRenderOverlay(RenderOverlayEvent event) {
        currentwatermarkmode = watermarkmodes.stream().anyMatch(watermarkMode -> watermarkMode.getName().equalsIgnoreCase(watermarkmode.getValue())) ? watermarkmodes.stream().filter(watermarkMode -> watermarkMode.getName().equalsIgnoreCase(watermarkmode.getValue())).findAny().get() : null ;
        currentarraylistmode = arraylistmodes.stream().anyMatch(arraylistMode -> arraylistMode.getName().equalsIgnoreCase(arraylistmode.getValue())) ? arraylistmodes.stream().filter(arraylistMode -> arraylistMode.getName().equalsIgnoreCase(arraylistmode.getValue())).findAny().get() : null ;
        currentinfomode = infomodes.stream().anyMatch(arraylistMode -> arraylistMode.getName().equalsIgnoreCase(infomode.getValue())) ? infomodes.stream().filter(infoMode -> infoMode.getName().equalsIgnoreCase(infomode.getValue())).findAny().get() : null ;
        currentwatermarkmode.onRenderOverlay(event);
        currentarraylistmode.onRenderOverlay(event);
        currentinfomode.onRenderOverlay(event);
    }
}
