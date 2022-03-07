package today.sleek.client.modules.impl.player;

import com.google.common.eventbus.Subscribe;
import today.sleek.base.event.impl.ChatEvent;
import today.sleek.base.modules.ModuleCategory;
import today.sleek.base.modules.ModuleData;
import today.sleek.client.irc.IRCClient;
import today.sleek.client.modules.impl.Module;
import today.sleek.client.utils.math.Stopwatch;

import java.net.URISyntaxException;

@ModuleData(
        name = "IRC",
        category = ModuleCategory.PLAYER,
        description = "Let's you chat with other client users"
)
public class IRC extends Module {

    public IRC() {
        super("IRC", ModuleCategory.PLAYER);
    }

    private IRCClient client;
    Stopwatch time = new Stopwatch();


    public void onEnable() {
        time.resetTime();

        try {
            client = new IRCClient();
            client.connectBlocking();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onDisable() {
        client.close();
        client = null;
    }

    @Subscribe
    public void onChat(ChatEvent event) {
        String message = event.getMessage();

        if (message.startsWith("-") || message.startsWith("- ")) {
            event.setCancelled(true);
            client.send(event.getMessage().replace("-", ""));
        }
    }
}
