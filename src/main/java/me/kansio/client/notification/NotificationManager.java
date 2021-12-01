package me.kansio.client.notification;

import dorkbox.messageBus.annotations.Subscribe;
import lombok.Getter;
import me.kansio.client.Client;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.utils.chat.ChatUtil;

import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager {
    @Getter
    private static NotificationManager notificationManager = new NotificationManager();

    @Getter private CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();

    public NotificationManager() {
        notifications.clear();
        Client.getInstance().getEventBus().subscribe(this);
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public void addNotification(String mainText, String title, long stayTime, int color) {
        this.addNotification(new Notification(mainText, title, stayTime, color));
    }

    @Subscribe
    public void onRenderOverlay(RenderOverlayEvent event) {
        if (!notifications.isEmpty()) {
            for (Notification notification : notifications) {
                boolean stopwatchEnabled = notification.inPlace && notification.prevInPlaceValue;
                if (notification.inPlace && !notification.prevInPlaceValue) {
                    notification.getStopwatch().resetTime();
                    ChatUtil.log("calllled");
                }
                if (notification.getStopwatch().timeElapsed(notification.getStayTime()) && stopwatchEnabled) {

                    notification.leavingAnimation(event);
                } else {
                    notification.render(event);
                }
               /* if (notification.getX() != event.getSr().getScaledWidth() -  notification.getWidth() - 13) {
                    notification.setX(notification.getX() + 1);
                }*/

            }
        }
    }
}
