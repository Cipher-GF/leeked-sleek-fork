package me.kansio.client.notification;
import lombok.*;
import me.kansio.client.event.impl.RenderOverlayEvent;
import me.kansio.client.utils.Stopwatch;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.render.ColorPalette;
import me.kansio.client.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.Render;

import java.awt.*;

/**
 * @author Divine
 *
 * I would paste superblaubeerie7 notification system but that means im not learning
 *
 */
public class Notification {

    private String mainText;
    private String title;
    private long stayTime;
    private int color;
    private int x;
    private int y;
    @Getter @Setter private int width;
    @Getter boolean inPlace;
    @Getter boolean prevInPlaceValue;
    private Stopwatch stopwatch;
    double thing = 15;

    public Notification(String mainText, String title, long stayTime, int color) {
        this.mainText = mainText;
        this.title = title;
        this.stayTime = stayTime;
        this.color = color;
        this.width = 15 + Math.max(Minecraft.getMinecraft().fontRendererObj.getStringWidth(mainText), Minecraft.getMinecraft().fontRendererObj.getStringWidth(title));
        this.stopwatch = new Stopwatch();
        inPlace = false;
        prevInPlaceValue = false;
        x = new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() - 2;
        y = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - 65;
        stopwatch.resetTime();
    }

    public void render(RenderOverlayEvent event) {

        prevInPlaceValue = isInPlace();
        double supposedX = event.getSr().getScaledWidth() - 2 - width;
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

        if (x > supposedX) {
            x -= 3;

        }
        if (x <= supposedX) {
            inPlace = true;
        }
        RenderUtils.drawBorderedRoundedRect(
                x,
                y,
                width,
                60,
                10,
                1,
                new Color(color).darker().getRGB(),
                ColorPalette.DARK_GREY.getColor().getRGB()
        );


        if (prevInPlaceValue) {
            ChatUtil.log(thing + " " + stopwatch.getTimeRemaining(stayTime));
            RenderUtils.drawRoundedRect(x, y + 50, thing, 10, 10, color);
            thing += ((float)(stayTime)) / 1000;
        }
    }

    public void leavingAnimation(RenderOverlayEvent event) {
        //double oldX = event.getSr().getScaledWidth() - 2 - width;
        double finishedX = event.getSr().getScaledWidth() + 10;

        if (x != finishedX) {
            x += 5;
        }

        if (x >= finishedX) {
            NotificationManager.getNotificationManager().getNotifications().remove(this);
            return;
        }
        RenderUtils.drawBorderedRoundedRect(
                x,
                y,
                width,
                60,
                10,
                1,
                new Color(color).darker().getRGB(),
                ColorPalette.DARK_GREY.getColor().getRGB()
        );
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStayTime() {
        return stayTime;
    }

    public void setStayTime(long stayTime) {
        this.stayTime = stayTime;
    }

    public int getColor() {
        return color;
    }

    public Stopwatch getStopwatch() {
        return stopwatch;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
