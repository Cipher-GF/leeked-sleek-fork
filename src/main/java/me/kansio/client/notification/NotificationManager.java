package me.kansio.client.notification;

import dorkbox.messageBus.annotations.Subscribe;
import lombok.Getter;
import me.kansio.client.Client;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.modules.impl.visuals.HUD;

import java.util.concurrent.LinkedBlockingQueue;

public class NotificationManager {

    @Getter private static NotificationManager notificationManager = new NotificationManager();

    @Getter private LinkedBlockingQueue<Notification> pendingNotifications = new LinkedBlockingQueue<>();

    @Getter private Notification currentNotification = null;

    public NotificationManager() {
        Client.getInstance().getEventBus().subscribe(this);
    }

    public void show(Notification notification) {
        pendingNotifications.add(notification);
    }

    public void update() {
        if (currentNotification != null && !currentNotification.isShown()) {
            currentNotification = null;
        }

        if (currentNotification == null && !pendingNotifications.isEmpty()) {
            currentNotification = pendingNotifications.poll();
            currentNotification.show();
        }

    }

    @Subscribe
    public void render(RenderOverlayEvent event) {
        if (!HUD.notifications) return;
        update();

        if (currentNotification != null)
            currentNotification.render();
    }
}
