package today.sleek.client.modules.impl.player;

import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import today.sleek.base.event.impl.UpdateEvent;
import today.sleek.base.modules.ModuleCategory;
import today.sleek.base.modules.ModuleData;
import today.sleek.client.gui.notification.Notification;
import today.sleek.client.gui.notification.NotificationManager;
import today.sleek.client.modules.impl.Module;
import today.sleek.client.utils.math.Stopwatch;
import today.sleek.client.utils.network.HttpUtil;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@ModuleData(
        name = "Ban Checker",
        description = "Checks how many people got staff banned recently",
        category = ModuleCategory.PLAYER
)
public class BanChecker extends Module {

    private int lastStaffBans = 0;
    private int currentStaffBans = 0;
    private Stopwatch watch = new Stopwatch();

    @Override
    public void onEnable() {
        watch.resetTime();
        lastStaffBans = 0;
        currentStaffBans = lastStaffBans;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (lastStaffBans == 0 || watch.timeElapsed(TimeUnit.SECONDS.toMillis(30))) {
            try {
                if (mc.thePlayer.ticksExisted < 5) {
                    HashMap<String, String> hesad = new HashMap<>();
                    hesad.put("API-Key", "96dd5933-7d2a-422e-855f-ec43b9a23b85");
                    JsonElement node = new JsonParser().parse(HttpUtil.get("https://api.hypixel.net/punishmentstats", hesad));

                    lastStaffBans = lastStaffBans == 0 ? node.getAsJsonObject().get("staff_total").getAsInt() : currentStaffBans;
                    currentStaffBans = node.getAsJsonObject().get("staff_total").getAsInt();

                    NotificationManager.getNotificationManager().show(new Notification(Notification.NotificationType.WARNING, "Staff bans", (currentStaffBans - lastStaffBans) + " new bans", 1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            watch.resetTime();
        }
    }
}
