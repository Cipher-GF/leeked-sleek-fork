package net.minecraft.client.gui;

import today.sleek.client.gui.MainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.core.util.SystemNanoClock;
import today.sleek.client.gui.alt.GuiCredits;
import today.sleek.client.gui.clickgui.utils.render.animation.easings.Animate;
import today.sleek.client.gui.clickgui.utils.render.animation.easings.Easing;
import today.sleek.client.utils.font.Fonts;
import today.sleek.client.utils.font.MCFontRenderer;
import today.sleek.client.utils.render.ColorUtils;
import today.sleek.client.utils.render.RenderUtil;

import java.awt.*;

public class GuiButton extends Gui {

    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    private final Animate moduleAnimation;
    /**
     * Button width in pixels
     */
    protected int width;

    /**
     * Button height in pixels
     */
    protected int height;

    /**
     * The x position of this control.
     */
    public int xPosition;

    /**
     * The y position of this control.
     */
    public int yPosition;

    /**
     * The string displayed on this control.
     */
    public String displayString;
    public int id;

    /**
     * True if this control is enabled, false to disable.
     */
    public boolean enabled;

    /**
     * Hides the button completely if false.
     */
    public boolean visible;
    protected boolean hovered;

//    public GuiButton(int buttonId, int x, int y, String buttonText)
//    {
//        this(buttonId, x, y, 203, 20, buttonText);
//    }

    float stringLen;

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this.moduleAnimation = new Animate();
        this.width = widthIn;
        this.height = heightIn;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.displayString = buttonText;
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver) {
        int i = 1;

        if (!this.enabled) {
            i = 0;
        } else if (mouseOver) {
            i = 2;
        }

        return i;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            stringLen = Fonts.Verdana.getStringWidth(this.displayString);
            moduleAnimation.setMin(0).setMax(60f).setReversed(!this.hovered).setEase(Easing.QUAD_IN_OUT);
            moduleAnimation.setReversed(!this.hovered);
            moduleAnimation.setSpeed(120).update();
            final MCFontRenderer font = Fonts.Verdana;
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            float boxWidth = this.xPosition + this.width / 2F - 99.8f;
//            RenderUtil.drawRect(this.xPosition + this.width / 2F - 79f, this.yPosition, 261 - this.width / 2F, 20, 0x80000000);

            int j = 14737632;

            if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = ColorUtils.getIntGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 20)) / 100D) + 9F / mc.fontRendererObj.FONT_HEIGHT * 9.95);
            }

            if (mc.currentScreen instanceof MainMenu) {
                RenderUtil.drawRoundedRect(this.xPosition + this.width / 2F - 220f, this.yPosition+1, (300 - this.width / 2F)-1, 20,5, 0x80000000);
                RenderUtil.drawOutlinedRoundedRect(this.xPosition + this.width / 2F - 220f, this.yPosition+1, (300 - this.width / 2F)-1, 20,5,1, ColorUtils.getIntGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 20)) / 100D) + 9F / mc.fontRendererObj.FONT_HEIGHT * 9.95));

                if (this.hovered) {
                    Fonts.Verdana.drawCenteredString(this.displayString, this.xPosition+13, this.yPosition + (this.height - 4f) / 2, j);
                } else {
                    Fonts.Verdana.drawCenteredString(this.displayString, this.xPosition+13, this.yPosition + (this.height - 4f) / 2, j);
                }
            } else if (mc.currentScreen instanceof GuiMainMenu) {
                RenderUtil.drawRoundedRect(this.xPosition + this.width / 2F - 75f, this.yPosition, ((300 - this.width / 2F)/1.335)-18, 20,5, 0x80000000);
                RenderUtil.drawOutlinedRoundedRect(this.xPosition + this.width / 2F - 75f, this.yPosition, ((300 - this.width / 2F)/1.335)-18, 20,5,1, ColorUtils.getIntGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 20)) / 100D) + 9F / mc.fontRendererObj.FONT_HEIGHT * 9.95));


                Fonts.Verdana.drawCenteredString(this.displayString, (this.xPosition + (this.width / 2f))-1, (this.yPosition + (this.height - 4f) / 2)-1, j);
//                RenderUtil.drawRect(this.xPosition + (this.width / 3f) + 34.8, this.yPosition + this.height - 1, -(int) moduleAnimation.getValue(), 1, ColorUtils.getIntGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + 9F / mc.fontRendererObj.FONT_HEIGHT * 9.95));
            } else if (mc.currentScreen instanceof GuiCredits) {
                RenderUtil.drawRoundedRect(this.xPosition + this.width / 2F - 75f, this.yPosition, ((300 - this.width / 2F)/1.335)-18, 20,5, 0x80000000);
                RenderUtil.drawOutlinedRoundedRect(this.xPosition + this.width / 2F - 75f, this.yPosition, (((300 - this.width / 2F)/1.335)-18), 20,5,1, ColorUtils.getIntGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 20)) / 100D) + 9F / mc.fontRendererObj.FONT_HEIGHT * 9.95));


                Fonts.Verdana.drawCenteredString(this.displayString, (this.xPosition + (this.width / 2f))-1, (this.yPosition + (this.height - 4f) / 2)-1, j);
//                RenderUtil.drawRect(this.xPosition + (this.width / 3f) + 34.8, this.yPosition + this.height - 1, -(int) moduleAnimation.getValue(), 1, ColorUtils.getIntGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + 9F / mc.fontRendererObj.FONT_HEIGHT * 9.95));
            } else {
                int i = 1;
                this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
                this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
                Fonts.Verdana.drawCenteredString(this.displayString, (this.xPosition + (this.width / 2f))-1, (this.yPosition + (this.height - 4f) / 2)-1, j);
            }


            this.mouseDragged(mc, mouseX, mouseY);


            // center the text in the button on depending on its length
//            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + (this.width / 2), this.yPosition + (this.height - 8) / 2, j);
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY) {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver() {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
