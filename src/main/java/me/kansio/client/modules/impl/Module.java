package me.kansio.client.modules.impl;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import me.kansio.client.Client;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.visuals.ClickGUI;
import me.kansio.client.notification.Notification;
import me.kansio.client.notification.NotificationManager;
import me.kansio.client.property.Value;
import me.kansio.client.property.value.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public abstract class Module {

    protected static final Minecraft mc = Minecraft.getMinecraft();

    private String name;
    private boolean toggled;
    private int keyBind;
    private String suffix = "";
    private ModuleCategory category;
    private List<SubSettings> subSettings = new ArrayList<>();

    public Module(String name, int keyBind, ModuleCategory category) {
        this.category = category;
        this.keyBind = keyBind;
        this.name = name;
    }

    public Module(String name, ModuleCategory category) {
        this(name, Keyboard.KEY_NONE, category);
    }

    public void toggle() {
        toggled = !toggled;

        if (toggled) {
            Client.getInstance().getEventBus().subscribe(this);
            onEnable();
        } else {
            Client.getInstance().getEventBus().unsubscribe(this);
            onDisable();
        }
        if (!(this instanceof ClickGUI))
        NotificationManager.getNotificationManager().show(new Notification(Notification.NotificationType.INFO, getName(), toggled ? "Enabled" : "Disabled", 1));
        onToggled();
    }

    public String getSuffix() {
        return suffix;
    }

    public void onToggled() {

    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void registerSubSettings(SubSettings... subSettings) {
        Collections.addAll(this.subSettings, subSettings);
    }

    public void register(Value... properties) {
        System.out.println(Client.getInstance().getValueManager());
        Collections.addAll(Client.getInstance().getValueManager().getObjects(), properties);
    }

    public void unRegister(Value... properties) {
        Collections.addAll(Client.getInstance().getValueManager().getObjects(), properties);
        Client.getInstance().getValueManager().getObjects().removeAll(Arrays.asList(properties));
    }

    public List<Value> getValues() {
        return Client.getInstance().getValueManager().getValuesFromOwner(this);
    }

    public void load(JsonObject obj) {
        obj.entrySet().forEach(ogzk -> {
            switch (ogzk.getKey()) {
                case "name": {
                    break;
                }
                case "enabled": {
                    toggled = ogzk.getValue().getAsBoolean();

                    if (toggled) {
                        Client.getInstance().getEventBus().subscribe(this);
                        onEnable();
                    } else {
                        Client.getInstance().getEventBus().unsubscribe(this);
                        onDisable();
                    }
                    if (!(this instanceof ClickGUI))
                        NotificationManager.getNotificationManager().show(new Notification(Notification.NotificationType.INFO, getName(), toggled ? "Enabled" : "Disabled", 1));
                    onToggled();
                    break;
                }
            }

            Value val = Client.getInstance().getValueManager().getValueFromOwner(this, ogzk.getKey());

            if (val != null) {
                if (val instanceof BooleanValue) {
                    val.setValue(ogzk.getValue().getAsBoolean());
                } else if (val instanceof NumberValue) {
                    val.setValue(ogzk.getValue().getAsDouble());
                } else if (val instanceof ModeValue) {
                    val.setValue(ogzk.getValue().getAsString());
                } else if (val instanceof StringValue) {
                    val.setValue(ogzk.getValue().getAsString());
                }
            }

        });
    }

    public JsonObject save() {
        JsonObject json = new JsonObject();
        json.addProperty("name", this.name);
        json.addProperty("enabled", this.toggled);
        getValues().forEach(value -> json.addProperty(value.getName(), value.getValue().toString()));
        return json;
    }

    public JsonObject saveKeybind() {
        JsonObject json = new JsonObject();
        json.addProperty("name", this.name);
        json.addProperty("keybind", this.keyBind);
        json.addProperty("keybindName", Keyboard.getKeyName(this.keyBind));

        return json;
    }

    public void setKeyBind(int keyBind) {
        System.out.println("Saved Config " + this.name);
        this.keyBind = keyBind;
        Client.getInstance().getKeybindManager().save();
    }
}
