package net.minecraft.client.gui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import negroidslayer.NegroidFarm;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import sun.misc.Unsafe;
import today.sleek.Sleek;
import today.sleek.base.protection.ProtectionUtil;
import today.sleek.client.gui.MainMenu;
import today.sleek.client.utils.font.Fonts;
import today.sleek.client.utils.font.MCFontRenderer;
import today.sleek.client.utils.glsl.GLSLSandboxShader;
import today.sleek.client.utils.network.HttpUtil;
import today.sleek.client.utils.render.ColorPalette;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GuiMainMenu extends GuiScreen {
    private GuiTextField username;
    private GLSLSandboxShader backgroundShader;
    private long initTime = System.currentTimeMillis();

    public GuiMainMenu() {
        try {
            this.backgroundShader = new GLSLSandboxShader("/background.fsh");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        try {
            if (ProtectionUtil.husdhuisgfhusgdrhuifosdguhisfgdhuisfgdhsifgduhsufgidsfdhguisfgdhuoisfguhdiosgfoduhisfghudiugfsidshofugid()) {
                System.out.println("Debugger found, exitting.");
                JOptionPane.showMessageDialog(null,
                        "Please disable any debuggers before running Sleek!",
                        "Error!",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
                return;
            }

            //tamper protection
            /*/if (!ProtectionUtil.gsudfgyfuisadgfdsouaiygsdeugdsoygfsdhohiusdfhuisdghiudgshiufssfdhiushudsdfuhfdshufdshuisfdhsfdhiusfdhuifsdhuifsdhuisfdhiufsdhiufsdhiusfdhuisfdhuifsdhuifsdhuifsdhiufsdiuhfsdhiufdshuisfdhui()) {
                JOptionPane.showMessageDialog(null,
                        "This version seems to be outdated. Please re-download!",
                        "Error!",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
                return;
            }/*/
            if (button.id == 0) {
                Map<String, String> header = new HashMap<>();
                HashMap<String, String> map = new HashMap<>();
                map.put("Client-Token", "s59gtK6FCntT6tafCNbyMpQ2");
                Sleek.getInstance().setUid(username.getText());
                String serv = HttpUtil.get("https://sleekapi.realreset.repl.co/api/user?hwid=" + NegroidFarm.guisdafghiusfgfsdhusdfghifsdhuidsfhuifdshuifsdhiudsfhiusfdhsdiuffsdhiudhsifusdfhiufsdhiufsdhiusdfhiufsdhiufsdhiu(), map);
                // get first object in array
                JsonObject json = new JsonParser().parse(serv).getAsJsonArray().get(0).getAsJsonObject();
                if (json.get("uid").getAsString().equals(Sleek.getInstance().getUid())) {
                    if (json.get("hwid").getAsString().equals(NegroidFarm.guisdafghiusfgfsdhusdfghifsdhuidsfhuifdshuifsdhiudsfhiusfdhsdiuffsdhiudhsifusdfhiufsdhiufsdhiusdfhiufsdhiufsdhiu())) {
                        Sleek.getInstance().onStart();
                        mc.displayGuiScreen(new MainMenu());
                        Sleek.getInstance().setUsername(json.get("username").getAsString());
                        Sleek.getInstance().setDiscordTag(String.format("%s#%s", new JsonParser().parse(HttpUtil.get("https://sleekapi.realreset.repl.co/api/getdiscordinfo?id=" + json.get("discordID").getAsString())).getAsJsonObject().get("username"), new JsonParser().parse(HttpUtil.get("https://sleekapi.realreset.repl.co/api/getdiscordinfo?id=" + json.get("discordID").getAsString())).getAsJsonObject().get("discriminator")));
                        Sleek.getInstance().setRank(json.get("rank").getAsString());
                    }
                } else {
                    System.out.println(serv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ProtectionUtil.husdhuisgfhusgdrhuifosdguhisfgdhuisfgdhsifgduhsufgidsfdhguisfgdhuoisfguhdiosgfoduhisfghudiugfsidshofugid()) {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                Unsafe unsafe = (Unsafe) f.get(null);
                unsafe.putAddress(0, 0);
            } catch (Exception e) {

            }
        }
    }
    public static boolean isAppInFullScreen()
    {
        WinDef.HWND foregroundWindow = User32.INSTANCE.GetForegroundWindow();
        WinDef.RECT foregroundRectangle = new WinDef.RECT();
        WinDef.RECT desktopWindowRectangle = new WinDef.RECT();
        User32.INSTANCE.GetWindowRect( foregroundWindow, foregroundRectangle );
        WinDef.HWND desktopWindow = User32.INSTANCE.GetForegroundWindow();
        User32.INSTANCE.GetWindowRect( desktopWindow, desktopWindowRectangle );
        return foregroundRectangle.toString().equals( desktopWindowRectangle.toString() );
    }
    @Override
    public void drawScreen(int x, int y2, float z) {
        MCFontRenderer font = Fonts.clickGuiFont();
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        this.backgroundShader.useShader(width, height, x, y2, (System.currentTimeMillis() - initTime) / 1000f);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);

        GL11.glEnd();

        // Unbind shader
        GL20.glUseProgram(0);
        username.drawTextBox();
//        drawString(mc.fontRendererObj, "Client has been skidded by vncat", mc.fontRendererObj.getStringWidth("Client has been skidded by vncat") / 2, height - 60, ColorPalette.AMBER.getColor().getRGB());

        Fonts.Arial45.drawCenteredString("§lS", width / 2 - 24, height / 4 - 24, ColorPalette.BLUE.getColor().getRGB());
        Fonts.Arial40.drawCenteredString("leek", width / 2 + 4, height / 4 -22.5f, -1); // -1 = white
        Fonts.Arial40.drawCenteredString("§lLog", width / 2 - 13, height / 4 -3.5f, -1); // -1 = white
        Fonts.Arial40.drawCenteredString("in", width / 2 + 17, height / 4 - 2, ColorPalette.BLUE.getColor().getRGB());
        if (username.getText().isEmpty()) {
            font.drawStringWithShadow("UID", width / 2F - 96, 106 + 60, -7829368);
        }
        super.drawScreen(x, y2, z);

    }

    @Override
    public void initGui() {
//        mc.displayGuiScreen(new MainMenu());
        int var3 = height / 4 + 24;
        buttonList.add(new GuiButton(0, width / 2 - 100, 124 + 60, 203, 20,"Login to Sleek"));
        username = new GuiTextField(var3, mc.fontRendererObj, width / 2 - 100, 100 + 60, 200, 20);
        username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
        initTime = System.currentTimeMillis();
    }

    @Override
    protected void keyTyped(char character, int key) {
        username.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x, int y2, int button) {
        try {
            super.mouseClicked(x, y2, button);
        } catch (IOException e) {
            e.printStackTrace();
        }
        username.mouseClicked(x, y2, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        username.updateCursorCounter();
    }
}