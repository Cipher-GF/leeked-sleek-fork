package today.sleek;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import today.sleek.base.config.ConfigManager;
import today.sleek.base.event.impl.KeyboardEvent;
import today.sleek.base.event.impl.PacketEvent;
import today.sleek.base.event.impl.ServerJoinEvent;
import today.sleek.base.keybind.KeybindManager;
import today.sleek.base.manager.KillsultManager;
import today.sleek.base.manager.ValueManager;
import today.sleek.base.protection.ProtectionUtil;
import today.sleek.client.commands.CommandManager;
import today.sleek.client.friend.FriendManager;
import today.sleek.client.gui.config.ConfigurationGUI;
import today.sleek.client.modules.ModuleManager;
import today.sleek.client.modules.impl.Module;
import today.sleek.client.modules.impl.player.hackerdetect.CheckManager;
import today.sleek.client.modules.impl.visuals.ClickGUI;
import today.sleek.client.rank.UserRank;
import today.sleek.client.targets.TargetManager;
import today.sleek.client.utils.network.HttpUtil;
import viamcp.ViaMCP;
import viamcp.utils.JLoggerToLog4j;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sleek {
    private String username;
    private String uid;
    private String discordTag;
    private UserRank rank;
    private Map<String, String> users = new HashMap<>();
    private File dir;
    private static Sleek instance = new Sleek();

    public static Sleek instance() {
        return instance;
    }

    private EventBus eventBus = new EventBus("Sleek");
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private ConfigManager configManager;
    private KillsultManager killsultManager;
    private ValueManager valueManager;
    private KeybindManager keybindManager;
    private FriendManager friendManager;
    private CheckManager checkManager;
    private TargetManager targetManager;

    public void onStart() {
        Logger jLogger = new JLoggerToLog4j(LogManager.getLogger("checksum"));
        jLogger.log(Level.INFO, "current checksum: " + ProtectionUtil.huisdfhufisdhfiusdhifsudfsihdusdiuhsfdiusfdhuisdfiuhsdfhisfdhiufsdhui());
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
        //Set the killsult manager
        killsultManager = new KillsultManager();
        //load the killsults
        killsultManager.readKillSults();
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
            System.out.println("[Sleek] Failed to start ViaMCP");
        }
        /*
        try {
            IPCClient client = new IPCClient(937350566886137886L);
            client.setListener(new IPCListener() {
                @Override
                public void onReady(IPCClient client) {
                    RichPresence.Builder builder = new RichPresence.Builder();
                    builder.setState("UID: " + uid)
                            .setDetails("Destroying servers")
                            .setStartTimestamp(OffsetDateTime.now())
                            .setLargeImage("canary-large", "Discord Canary")
                            .setSmallImage("ptb-small", "Discord PTB");
                    client.sendRichPresence(builder.build());
                }
            });
            client.connect();
        } catch (Exception e) {
            System.out.println("Discord not found, not setting rpc.");
        }
        */
        System.out.println("Client has been started.");
        //set the window title
        Display.setTitle("Sleek Beta 030322");
    }

    public void onShutdown() {
        //leave
        try {
            System.out.println(HttpUtil.delete(MessageFormat.format("https://sleekapi.realreset.repl.co/api/leaveserver?clientname={0}", username)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //save keybinds
        if (keybindManager != null) {
            keybindManager.save();
        }
    }

    @Subscribe
    public void onChat(PacketEvent event) {
        if (event.getPacket() instanceof S02PacketChat) {
            S02PacketChat packet = event.getPacket();
            for (Map.Entry<String, String> user : users.entrySet()) {
                if (packet.getChatComponent().getUnformattedText().contains(user.getKey())) {
                    packet.chatComponent = new ChatComponentText(packet.getChatComponent().getFormattedText().replaceAll(user.getKey(), MessageFormat.format("§b{0} §7({1})", user.getValue(), user.getKey())));
                }
            }
        }
    }

    @Subscribe
    public void onJoin(ServerJoinEvent event) {
//        try {
//            System.out.println(HttpUtil.delete(MessageFormat.format("https://sleekapi.realreset.repl.co/api/leaveserver?clientname={0}", username)));
//            System.out.println(HttpUtil.post("https://sleekapi.realreset.repl.co/api/joinserver?name=" + this.username + "&uid=1" + "&ign=" + event.getIgn() + "&ip=" + event.getServerIP(), ""));
//            JsonElement node = new JsonParser().parse(HttpUtil.get("https://sleekapi.realreset.repl.co/api/getclientplayers"));
//
//            if (node.isJsonArray()) {
//                users.clear();
//                for (JsonElement ele : node.getAsJsonArray()) {
//                    JsonObject obj = ele.getAsJsonObject();
//
//                    users.put(obj.get("ign").getAsString(), obj.get("name").getAsString());
//
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void setRank(String rank) {
        switch (rank) {
        case "Developer": 
            {
                this.rank = UserRank.DEVELOPER;
                break;
            }
        case "Beta": 
            {
                this.rank = UserRank.BETA;
                break;
            }
        default: 
            {
                this.rank = UserRank.USER;
                break;
            }
        }
    }

    @Subscribe
    public void onKeyboard(KeyboardEvent event) {
        int key = event.getKeyCode();
        if (key == Keyboard.KEY_RSHIFT) {
            ClickGUI clickGUI = (ClickGUI) Sleek.getInstance().getModuleManager().getModuleByName("Click GUI");
            clickGUI.toggle();
        }
        if (key == Keyboard.KEY_INSERT) {
            Minecraft.getMinecraft().displayGuiScreen(new ConfigurationGUI());
        }
        //This handles keybinds.
        for (Module module : moduleManager.getModules()) {
            //check if the keybind is -1, if it is, just continue.
            if (module.getKeyBind() == -1) continue;
            //if the bind == the key, toggle the module
            if (module.getKeyBind() == key) {
                module.toggle();
            }
        }
    }

    @SuppressWarnings("all")
    public String getUsername() {
        return this.username;
    }

    @SuppressWarnings("all")
    public void setUsername(final String username) {
        this.username = username;
    }

    @SuppressWarnings("all")
    public String getUid() {
        return this.uid;
    }

    @SuppressWarnings("all")
    public void setUid(final String uid) {
        this.uid = uid;
    }

    @SuppressWarnings("all")
    public String getDiscordTag() {
        return this.discordTag;
    }

    @SuppressWarnings("all")
    public void setDiscordTag(final String discordTag) {
        this.discordTag = discordTag;
    }

    @SuppressWarnings("all")
    public UserRank getRank() {
        return this.rank;
    }

    @SuppressWarnings("all")
    public Map<String, String> getUsers() {
        return this.users;
    }

    @SuppressWarnings("all")
    public File getDir() {
        return this.dir;
    }

    @SuppressWarnings("all")
    public static Sleek getInstance() {
        return Sleek.instance;
    }

    @SuppressWarnings("all")
    public EventBus getEventBus() {
        return this.eventBus;
    }

    @SuppressWarnings("all")
    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    @SuppressWarnings("all")
    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    @SuppressWarnings("all")
    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    @SuppressWarnings("all")
    public KillsultManager getKillsultManager() {
        return this.killsultManager;
    }

    @SuppressWarnings("all")
    public ValueManager getValueManager() {
        return this.valueManager;
    }

    @SuppressWarnings("all")
    public KeybindManager getKeybindManager() {
        return this.keybindManager;
    }

    @SuppressWarnings("all")
    public FriendManager getFriendManager() {
        return this.friendManager;
    }

    @SuppressWarnings("all")
    public CheckManager getCheckManager() {
        return this.checkManager;
    }

    @SuppressWarnings("all")
    public TargetManager getTargetManager() {
        return this.targetManager;
    }
}
