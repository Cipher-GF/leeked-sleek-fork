package me.kansio.client.modules.api;

public enum ModuleCategory {

    COMBAT("Combat"),
    EXPLOIT("Exploit"),
    MOVEMENT("Movement"),
    VISUALS("Visuals"),
    PLAYER("Player"),
    MISC("Misc"),
    HIDDEN("Hidden");

    public String name;

    ModuleCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
