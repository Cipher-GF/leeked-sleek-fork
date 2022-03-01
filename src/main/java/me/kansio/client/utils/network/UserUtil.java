package me.kansio.client.utils.network;

import lombok.Getter;
import lombok.Setter;
import me.kansio.client.Client;

public class UserUtil {

    @Deprecated
    public static String getBuildType(int uid) {
        return uid <= 4 ? "Developer" : "Release";
    }

    public static String getBuildType() {
        return Integer.parseInt(Client.getInstance().getUid()) <= 4 ? "Developer" : "Release";
    }

}
