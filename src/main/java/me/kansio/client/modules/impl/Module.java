package me.kansio.client.modules.impl;

import lombok.Getter;
import lombok.Setter;
import me.kansio.client.Client;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.property.Value;
import net.minecraft.client.Minecraft;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter @Setter
public abstract class Module {

    protected static final Minecraft mc = Minecraft.getMinecraft();

    private String name;
    private boolean toggled;
    private int keyBind;
    private String suffix = "";
    private ModuleCategory category;

    public Module(String name, int keyBind, ModuleCategory category) {
        this.category = category;
        this.keyBind = keyBind;
        this.name = name;
    }

    public Module(String name, ModuleCategory category) {
        this.category = category;
        this.keyBind = -1;
        this.name = name;
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
}
