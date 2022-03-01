package me.kansio.client.event.impl;

import me.kansio.client.event.Event;

public class KeyboardEvent extends Event {
    private final int keyCode;

    public KeyboardEvent(int key) {
        this.keyCode = key;
    }

    @SuppressWarnings("all")
    public int getKeyCode() {
        return this.keyCode;
    }
}
