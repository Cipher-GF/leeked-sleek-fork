package me.kansio.client.event.impl;

import me.kansio.client.event.Event;

public class ServerJoinEvent extends Event {
    private String serverIP;
    private String ign;

    public ServerJoinEvent(String serverIP, String ign) {
        this.serverIP = serverIP;
        this.ign = ign;
    }

    @SuppressWarnings("all")
    public String getServerIP() {
        return this.serverIP;
    }

    @SuppressWarnings("all")
    public String getIgn() {
        return this.ign;
    }
}
