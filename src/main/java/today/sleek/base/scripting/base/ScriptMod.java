package today.sleek.base.scripting.base;

import jdk.nashorn.api.scripting.JSObject;
import today.sleek.base.event.Event;

import java.util.HashMap;
import java.util.Map;

public class ScriptMod {

    private final String name;
    private final String description;
    private final HashMap<String, JSObject> callbacks = new HashMap<>();

    public ScriptMod(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void onEvent(String eventName, Event e) {
        callbacks.get(eventName).call(e);
    }

    public void onEnable() {

    }

    public void onLoad() {

    }

    public void onDisable() {

    }

    public void on(String eventName, JSObject callback) {
        callbacks.put(eventName, callback);
    }

}

interface CallbackArg {
    <T extends Event> void call(T event);
}

interface Callback {
    void call();
}
