package me.kansio.client.gui;

import me.kansio.client.gui.alt.GuiAltManager;
import me.kansio.client.utils.font.Fonts;
import me.kansio.client.utils.glsl.GLSLSandboxShader;
import me.kansio.client.utils.render.ColorPalette;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.IOException;

public class MainMenu extends GuiScreen
{

    private static final ResourceLocation BACKGROUND = new ResourceLocation("sleek/images/background.png");
    private GLSLSandboxShader backgroundShader;
    private long initTime = System.currentTimeMillis();

    public MainMenu() {
        try {
            this.backgroundShader = new GLSLSandboxShader("/background.fsh");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initGui()
    {
        /*
        try {
            this.backgroundShader = new GLSLSandboxShader("")
        } catch (IOException e){
            throw new IllegalStateException("Failed To Load Main Menu Shader");
        }
         */
        int j = height / 4 + 48;
        int i = 24;
        this.buttonList.add(new GuiButton(0, width / 2 - 100, j, I18n.format("menu.singleplayer")));
        this.buttonList.add(new GuiButton(1, width / 2 - 100, j + i, I18n.format("menu.multiplayer")));
        this.buttonList.add(new GuiButton(2, width / 2 - 100, j + i * 2, "Alt Manager"));
        this.buttonList.add(new GuiButton(3, width / 2 - 100, j + 84, 98, 20, I18n.format("menu.options")));
        this.buttonList.add(new GuiButton(4, width / 2 + 2, j + 84, 98, 20, I18n.format("menu.quit")));
        initTime = System.currentTimeMillis();
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
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
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {

        GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        this.backgroundShader.useShader(this.width, this.height, mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000f);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);

        GL11.glEnd();

        // Unbind shader
        GL20.glUseProgram(0);
        Fonts.Arial45.drawCenteredString("§lS", width / 2 - 24, height / 4 -24, ColorPalette.BLUE.getColor().getRGB());
        Fonts.Arial40.drawCenteredString("leek", width / 2 + 4, height / 4 -22.5f, -1); // -1 = white
        String devinfo = "Made with <3 by Reset, Kansio, PC, Divine and Moshi";
        Fonts.Verdana.drawString(devinfo, (width - Fonts.Arial30.getStringWidth(devinfo)) + 135, height - 10, -1);
        GlStateManager.color(1.0F,1.0F,1.0F,1.0F);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
