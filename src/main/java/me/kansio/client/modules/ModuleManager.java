package me.kansio.client.modules;

import lombok.Getter;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.combat.KillAura;
import me.kansio.client.modules.impl.combat.Velocity;
import me.kansio.client.modules.impl.exploit.Disabler;
import me.kansio.client.modules.impl.movement.Flight;
import me.kansio.client.modules.impl.movement.Speed;
import me.kansio.client.modules.impl.player.NoFall;
import me.kansio.client.modules.impl.visuals.ClickGUI;
import me.kansio.client.modules.impl.visuals.HUD;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    @Getter
    private final ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        registerModules();
    }

    public void registerModules() {
        //Combat
        modules.add(new KillAura());
        modules.add(new Velocity());

        //Movement
        modules.add(new Speed());
        modules.add(new Flight());

        //Visual
        modules.add(new HUD());
        modules.add(new ClickGUI());

        //Exploit
        modules.add(new Disabler());

        //Player
        modules.add(new NoFall());


        //Toggle modules
        HUD hud = (HUD) getModuleByName("HUD");
        hud.toggle();
    }

    public void reloadModules() {
        for (Module mod : modules) {
            if (mod.isToggled())
                mod.toggle();
        }
        modules.clear();
        registerModules();
    }

    public List<Module> getModulesSorted(FontRenderer customFontRenderer) {
        List<Module> moduleList = new ArrayList<>(modules);
        moduleList.sort((a, b) -> {
            String dataA = a.getSuffix() == null ? "" : a.getSuffix();
            String dataB = b.getSuffix() == null ? "" : b.getSuffix();
            String nameA = a.getName();
            String nameB = b.getName();

            int first = (int) customFontRenderer.getStringWidth(nameA + dataA);
            int second = (int) customFontRenderer.getStringWidth(nameB + dataB);
            return second - first;
        });
        return moduleList;
    }

    public void sort(FontRenderer fontRenderer) {
        modules.sort((a, b) -> {
            String dataA = a.getSuffix() == null ? "" : a.getSuffix();
            String dataB = b.getSuffix() == null ? "" : b.getSuffix();
            int first = fontRenderer.getStringWidth(a.getName() + dataA);
            int second = fontRenderer.getStringWidth(b.getName() + dataB);
            return second - first;
        });
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
