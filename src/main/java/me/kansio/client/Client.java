package me.kansio.client;

import com.google.common.eventbus.EventBus;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dorkbox.messageBus.MessageBus;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import me.kansio.client.commands.CommandManager;
import me.kansio.client.config.ConfigManager;
import me.kansio.client.event.impl.KeyboardEvent;
import me.kansio.client.event.impl.ServerJoinEvent;
import me.kansio.client.friend.FriendManager;
import me.kansio.client.keybind.KeybindManager;
import me.kansio.client.manager.ValueManager;
import me.kansio.client.modules.ModuleManager;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.modules.impl.player.hackerdetect.CheckManager;
import me.kansio.client.modules.impl.visuals.ClickGUI;
import me.kansio.client.targets.TargetManager;
import me.kansio.client.utils.network.HttpUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import viamcp.ViaMCP;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class Client {

    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String uid;
    @Getter
    @Setter
    private String discordTag;
    @Getter
    private Map<String, String> users = new HashMap<>();

    @Getter
    private File dir;

    @Getter
    private static Client instance = new Client();

    @Getter
    private EventBus eventBus = new EventBus("Sleek");

    @Getter
    private ModuleManager moduleManager;

    @Getter
    private CommandManager commandManager;

    @Getter
    private ConfigManager configManager;

    @Getter
    private ValueManager valueManager;

    @Getter
    private KeybindManager keybindManager;

    @Getter
    private FriendManager friendManager;

    @Getter
    private CheckManager checkManager;

    @Getter
    private TargetManager targetManager;

    public void onStart() {
        //Set the client file directory
        dir = new File(Minecraft.getMinecraft().mcDataDir, "Sleek");

        //Subscribe to the event bus
        eventBus.register(this);

        //Set the value manager
        valueManager = new ValueManager();

        //Set the module manager variable
        moduleManager = new ModuleManager();

        //Set the command manager
        commandManager = new CommandManager();

        //Set the config manager
        configManager = new ConfigManager(new File(dir, "configs"));

        //Set the keybind manager
        keybindManager = new KeybindManager(dir);
        //load the keybinds
        keybindManager.load();

        //Set the friend manager
        friendManager = new FriendManager();

        //set the target manager
        targetManager = new TargetManager();

        //Set the check manager
        checkManager = new CheckManager();

        //Setup ViaMCP
        try {
            ViaMCP.getInstance().start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Client has been started.");

        //set the window title
        Display.setTitle("Sleek v0.1");
    }

    public void onShutdown() {
        //save keybinds
        keybindManager.save();
    }

    @Subscribe
    public void onJoin(ServerJoinEvent event) {
        try {
            System.out.println(HttpUtil.delete(MessageFormat.format("http://zerotwoclient.xyz:13337/api/v1/leaveserver?clientname={0}", username)));

            System.out.println(HttpUtil.post("http://zerotwoclient.xyz:13337/api/v1/joinserver?name=" + this.username + "&uid=1" + "&ign=" + event.getIgn() + "&serverIP=" + event.getServerIP(), ""));
            JsonElement node = new JsonParser().parse(HttpUtil.get("http://zerotwoclient.xyz:13337/api/v1/getclientplayers"));

            if (node.isJsonArray()) {
                users.clear();
                for (JsonElement ele : node.getAsJsonArray()) {
                    JsonObject obj = ele.getAsJsonObject();

                    users.put(obj.get("ign").getAsString(), obj.get("name").getAsString());

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onKeyboard(KeyboardEvent event) {
        int key = event.getKeyCode();

        if (key == Keyboard.KEY_RSHIFT) {
            ClickGUI clickGUI = (ClickGUI) Client.getInstance().getModuleManager().getModuleByName("Click GUI");
            clickGUI.toggle();
        }

        //This handles keybinds.
        for (Module module : moduleManager.getModules()) {
            //check if the keybind is -1, if it is, just continue.
            if (module.getKeyBind() == -1)
                continue;

            //if the bind == the key, toggle the module
            if (module.getKeyBind() == key) {
                module.toggle();
            }
        }
    }

}
