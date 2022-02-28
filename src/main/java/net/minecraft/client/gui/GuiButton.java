package net.minecraft.client.gui;

import me.kansio.client.Client;
import me.kansio.client.gui.MainMenu;
import me.kansio.client.gui.clickgui.utils.render.animation.easings.Animate;
import me.kansio.client.gui.clickgui.utils.render.animation.easings.Easing;
import me.kansio.client.modules.impl.visuals.ClickGUI;
import me.kansio.client.utils.font.Fonts;
import me.kansio.client.utils.font.MCFontRenderer;
import me.kansio.client.utils.render.ColorPalette;
import me.kansio.client.utils.render.ColorUtils;
import me.kansio.client.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.Objects;

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

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        this.moduleAnimation = new Animate();
        moduleAnimation.setMin(0).setMax(80f).setReversed(!this.hovered).setEase(Easing.QUAD_IN_OUT);
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
            moduleAnimation.setReversed(!this.hovered);
            moduleAnimation.setSpeed(120).update();
            final MCFontRenderer font = Fonts.Verdana;
            FontRenderer fontRenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            float boxWidth = this.xPosition + this.width / 2F - 99.8f;
            float stringLen = fontRenderer.getStringWidth(this.displayString);
            RenderUtil.drawRect(this.xPosition + this.width / 2F - 79f, this.yPosition, 261 - this.width / 2F, 20, 0x80000000);
            if (mc.currentScreen instanceof MainMenu) {
                if (this.hovered) {
//                    RenderUtil.drawBottemRoundedRect(this.xPosition + this.width / 2F - 99.8f, this.yPosition, 300 - this.width / 2F, 20, 10, 0x80000000);
                    RenderUtil.drawRect(this.xPosition + (this.width / 3f) + 34.8, this.yPosition + this.height - 1, (int) moduleAnimation.getValue(), 1, ColorUtils.getIntGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + 9F / mc.fontRendererObj.FONT_HEIGHT * 9.95));
                    RenderUtil.drawRect(this.xPosition + (this.width / 3f) + 34.8, this.yPosition + this.height - 1, -(int) moduleAnimation.getValue(), 1, ColorUtils.getIntGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + 9F / mc.fontRendererObj.FONT_HEIGHT * 9.95));
                }
            } else {
//                RenderUtil.drawBottemRoundedRect(this.xPosition + this.width / 2F - 99.8f, this.yPosition, 300 - this.width / 2F, 20, 10, 0x80000000);
                RenderUtil.drawRect(boxWidth + 25, this.yPosition + this.height - 1, 250 - this.width / 2F, 1, ColorUtils.getIntGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + 9F / mc.fontRendererObj.FONT_HEIGHT * 9.95));
            }
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = 16777120;
            }
            Fonts.Verdana.drawCenteredString(this.displayString, this.xPosition + (this.width / 2f), this.yPosition + (this.height - 4f) / 2, j);
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
