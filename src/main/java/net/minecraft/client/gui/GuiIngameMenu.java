package net.minecraft.client.gui;

import me.kansio.client.gui.MainMenu;
import me.kansio.client.utils.font.Fonts;
import me.kansio.client.utils.render.ColorPalette;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

public class GuiIngameMenu extends GuiScreen
{
    private int field_146445_a;
    private int field_146444_f;

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        this.field_146445_a = 0;
        this.buttonList.clear();
        int i = -16;
        int j = 98;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + i, 203, 20,I18n.format("menu.returnToMenu", new Object[0])));

        if (!this.mc.isIntegratedServerRunning())
        {
            ((GuiButton)this.buttonList.get(0)).displayString = I18n.format("menu.disconnect", new Object[0]);
        }

        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + i, 203, 20,I18n.format("menu.returnToGame", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + i, 203, 20,I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 72 + i, 203, 20,I18n.format("gui.achievements", new Object[0])));
        this.buttonList.add(new GuiButton(6, this.width / 2 - 100, this.height / 4 + 48 + i, 203, 20,I18n.format("gui.stats", new Object[0])));
//        GuiButton guibutton;
//        this.buttonList.add(guibutton = new GuiButton(7, this.width / 2 + 100, this.height / 4 + 96 + i, 300, 20, I18n.format("menu.shareToLan", new Object[0])));
//        guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        switch (button.id)
        {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;

            case 1:
                boolean flag = this.mc.isIntegratedServerRunning();
                boolean flag1 = this.mc.func_181540_al();
                button.enabled = false;
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld((WorldClient)null);

                if (flag)
                {
                    this.mc.displayGuiScreen(new MainMenu());
                }
                else
                {
                    this.mc.displayGuiScreen(new GuiMultiplayer(new MainMenu()));
                }

            case 2:
            case 3:
            default:
                break;

            case 4:
                this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
                break;

            case 5:
                this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
                break;

            case 6:
                this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
                break;

            case 7:
                this.mc.displayGuiScreen(new GuiShareToLan(this));
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.field_146444_f;
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        Fonts.Arial45.drawCenteredString("§lS", width / 2 - 24, height / 4 - 24, ColorPalette.BLUE.getColor().getRGB());
        Fonts.Arial40.drawCenteredString("leek", width / 2 + 4, height / 4 -22.5f, -1); // -1 = white
//        this.drawCenteredString(this.fontRendererObj, I18n.format("menu.game", new Object[0]), this.width / 2, 40, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
