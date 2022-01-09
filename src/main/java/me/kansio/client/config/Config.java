package me.kansio.client.config;

import lombok.Getter;
import lombok.Setter;
import me.kansio.client.notification.Notification;
import me.kansio.client.notification.NotificationManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {

    @Getter @Setter private String name;
    @Getter @Setter private File file;

    public Config(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public void rename(String newName) {
        try {
            Path original = Paths.get(file.getCanonicalPath());
            Path to = Paths.get(file.getPath());

            Files.move(original, to);
        } catch (Exception e) {
            NotificationManager.getNotificationManager().show(new Notification(Notification.NotificationType.ERROR, "Error!", "Couldn't rename config!", 5));
            e.printStackTrace();
        }
    }

}
