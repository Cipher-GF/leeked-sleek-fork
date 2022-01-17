package me.kansio.client.event.impl;

import me.kansio.client.event.Event;

public class RenderEvent2 extends Event {
    private final float partialTicks;

    public RenderEvent2(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
