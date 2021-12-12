package me.kansio.client.modules;

import lombok.Getter;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.combat.Velocity;
import me.kansio.client.modules.impl.movement.Speed;
import me.kansio.client.modules.impl.visuals.ClickGUI;
import me.kansio.client.modules.impl.visuals.HUD;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    @Getter
    private final ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        //Combat
        modules.add(new Velocity());

        //Movement
        modules.add(new Speed());

        //Visual
        modules.add(new HUD());
        modules.add(new ClickGUI());



        //Toggle modules
        HUD hud = (HUD) getModuleByName("HUD");
        hud.toggle();
    }


    public Module getModuleByName(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    public List<Module> getModulesFromCategory(ModuleCategory category) {
        ArrayList<Module> mods = new ArrayList<>();
        for (Module module : modules) {
            if (module.getCategory() == category) {
                mods.add(module);
            }
        }
        return mods;
    }

}
