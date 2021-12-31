package me.kansio.client.modules;

import lombok.Getter;
import me.kansio.client.modules.api.ModuleCategory;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.combat.*;
import me.kansio.client.modules.impl.exploit.*;
import me.kansio.client.modules.impl.movement.*;
import me.kansio.client.modules.impl.player.*;
import me.kansio.client.modules.impl.visuals.*;
//import me.kansio.client.modules.impl.world.*;
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
        modules.add(new Criticals());

        //Exploit
        modules.add(new Disabler());
        modules.add(new ACDetector());

        //Movement
        modules.add(new Speed());
        modules.add(new Flight());
        modules.add(new Scaffold());
        modules.add(new InvMove());

        //Player
        modules.add(new NoFall());
        modules.add(new Sprint());
        modules.add(new ChestStealer());
        modules.add(new InvManager());
        modules.add(new Timer());
        modules.add(new NoRotate());

        //Visual
        modules.add(new HUD());
        modules.add(new ClickGUI());

        //World



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
