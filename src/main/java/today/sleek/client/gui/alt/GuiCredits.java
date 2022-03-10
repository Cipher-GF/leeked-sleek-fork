package today.sleek.client.gui.alt;

import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import today.sleek.client.utils.chat.NameUtil;
import today.sleek.client.utils.font.Fonts;
import today.sleek.client.utils.glsl.GLSLSandboxShader;
import today.sleek.client.utils.render.ColorPalette;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.net.URI;

import java.io.IOException;

public class GuiCredits extends GuiScreen {
    private final GuiScreen previousScreen;

    private GLSLSandboxShader backgroundShader;
    private long initTime = System.currentTimeMillis();
    private final int j = Math.round(height / 1.5F);

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
        this.buttonList.add(new GuiButton(1, width / 2 - 70, j +100 ,     150, 20,I18n.format("Go Back")));

        initTime = System.currentTimeMillis();
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case 1:
                mc.displayGuiScreen(previousScreen);
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiAltManager(this));
                break;
            case 3:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 4:
                this.mc.shutdown();
                break;
            case 5:
                this.mc.shutdown();
                break;
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
        Fonts.Arial45.drawCenteredString("§lS", width / 2 - 20, height / 4 -24, ColorPalette.BLUE.getColor().getRGB());
        Fonts.Arial40.drawCenteredString("leek", width / 2 +8, height / 4 -22.5f, -1); // -1 = white
        Fonts.Arial40.drawCenteredString("§lC", width / 2 - 13, height / 4 -3.5f, -1); // -1 = white
        Fonts.Arial40.drawCenteredString("redits", width / 2 + 17, height / 4 - 2, ColorPalette.BLUE.getColor().getRGB());
//        Fonts.Verdana.drawString(devinfo, (width - Fonts.Arial30.getStringWidth(devinfo)) + 110, height - 10, -1);
        String devinfo = "Made with <3 by Reset, Kansio, nullswap, Divine and qoft";
        Fonts.Verdana.drawCenteredString(devinfo, width - 150, height - 10, -1);
        String text = "hi";
//        Fonts.Verdana.drawString(devinfo, (width - Fonts.Arial30.getStringWidth(devinfo)) + 135, height - 10, -1);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}