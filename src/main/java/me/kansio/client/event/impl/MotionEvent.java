package me.kansio.client.event.impl;

import me.kansio.client.event.Event;

public class MotionEvent extends Event {
    private boolean isPre;
    private float yaw;
    private float pitch;
    private double y;
    private boolean onground;
    private boolean alwaysSend;


    public boolean isPre() {
        return this.isPre;
    }

    public boolean isPost() {
        return !this.isPre;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public double getY() {
        return this.y;
    }

    public boolean isOnground() {
        return this.onground;
    }

    public boolean shouldAlwaysSend() {
        return this.alwaysSend;
    }

    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public void setGround(final boolean ground) {
        this.onground = ground;
    }

    public void setAlwaysSend(final boolean alwaysSend) {
        this.alwaysSend = alwaysSend;
    }
}

