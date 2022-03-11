package today.sleek.client.gui.alt;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import today.sleek.client.utils.font.Fonts;
import today.sleek.client.utils.glsl.GLSLSandboxShader;
import today.sleek.client.utils.render.ColorPalette;
import today.sleek.client.utils.render.ColorUtils;
import today.sleek.client.utils.render.RenderUtil;

import java.awt.*;
import java.io.IOException;

public class GuiCredits extends GuiScreen {
    private final GuiScreen previousScreen;

    private GLSLSandboxShader backgroundShader;
    private long initTime = System.currentTimeMillis();
    private final int j = Math.round(height / 1.5F);

    /**
     * The x position of this control.
     */
    public int xPosition;

    /**
     * The y position of this control.
     */
    public int yPosition;

    public GuiCredits(GuiScreen previousScreen) {
        try {
            this.backgroundShader = new GLSLSandboxShader("/menu.fsh");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.previousScreen = previousScreen;
    }

    public void initGui() {
        /*
        try {
            this.backgroundShader = new GLSLSandboxShader("")
        } catch (IOException e){
            throw new IllegalStateException("Failed To Load Main Menu Shader");
        }
         */
        int j = height / 4+45;
        this.buttonList.add(new GuiButton(0, width / 2 - 65, j +175, 150, 20,I18n.format("Go Back")));

        initTime = System.currentTimeMillis();
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            mc.displayGuiScreen(previousScreen);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        this.backgroundShader.useShader(width, height, mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);

        GL11.glEnd();

        // Unbind shader
        GL20.glUseProgram(0);
        double boxWidth = (-(300 - this.width / 2F)*2);
        double boxHeight = 300;
        double boxX = this.xPosition + this.width / 2F - 175f;
        double boxY = this.yPosition+90;
        RenderUtil.drawRoundedRect(        boxX, boxY,  boxWidth,  boxHeight,5, 0x80000000);
        RenderUtil.drawOutlinedRoundedRect(boxX, boxY,  boxWidth , boxHeight,5,2, ColorUtils.getIntGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 20)) / 100D) + 9F / mc.fontRendererObj.FONT_HEIGHT * 9.95));
        Fonts.Arial45.drawCenteredString("§lS", width / 2 - 20, height / 4 -24, ColorPalette.BLUE.getColor().getRGB());
        Fonts.Arial40.drawCenteredString("leek", width / 2 +8, height / 4 -22.5f, -1); // -1 = white
        Fonts.Arial40.drawCenteredString("§lC", width / 2 - 20, height / 4 -3.5f, -1); // -1 = white
        Fonts.Arial40.drawCenteredString("redits", width / 2 + 16, height / 4 - 4, ColorPalette.BLUE.getColor().getRGB());
//        Fonts.Verdana.drawString(devinfo, (width - Fonts.Arial30.getStringWidth(devinfo)) + 110, height - 10, -1);


        String devInfo[] = {
          "We are a development team of retarded people.",
          "",
          "Kansio - Lead Developer",
          "Divine - Sex Developer",
          "Reset - Amazing person",
          "Qoft - Sex maker",
          "Nullswap - Bypasses",

        };
        for (int i = 0; i < devInfo.length; i++) {
            String x = devInfo[i];

            Fonts.Arial30.drawCenteredString(x, (width) / 2f + 4, (this.height /2 + (i*25))/1.5, ColorUtils.getIntGradientOffset(new Color(255, 255, 255), new Color(172, 172, 172), (Math.abs(((System.currentTimeMillis()) / 20)) / 100D) + 9F / mc.fontRendererObj.FONT_HEIGHT * 9.95));
        }


        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}