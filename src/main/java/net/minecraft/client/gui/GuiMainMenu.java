package net.minecraft.client.gui;

import java.util.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.kansio.client.Client;
import me.kansio.client.gui.MainMenu;
import me.kansio.client.utils.font.Fonts;
import me.kansio.client.utils.font.MCFontRenderer;
import me.kansio.client.utils.network.HttpUtil;
import me.kansio.client.utils.render.ColorPalette;
import negroidslayer.NegroidFarm;
import org.lwjgl.input.Keyboard;
import java.io.IOException;

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
            if (button.id == 0) {
                Map<String, String> header = new HashMap<>();
                HashMap<String, String> map = new HashMap<>();
                map.put("Client-Token", "s59gtK6FCntT6tafCNbyMpQ2");
                Client.getInstance().setUid(username.getText());
                String serv = HttpUtil.get("https://sleekapi.realreset.repl.co/api/user?hwid=" + NegroidFarm.guisdafghiusfgfsdhusdfghifsdhuidsfhuifdshuifsdhiudsfhiusfdhsdiuffsdhiudhsifusdfhiufsdhiufsdhiusdfhiufsdhiufsdhiu(), map);
                // get first object in array
                JsonObject json = new JsonParser().parse(serv).getAsJsonArray().get(0).getAsJsonObject();
                if (json.get("uid").getAsString().equals(Client.getInstance().getUid())) {
                    if (json.get("hwid").getAsString().equals(NegroidFarm.guisdafghiusfgfsdhusdfghifsdhuidsfhuifdshuifsdhiudsfhiusfdhsdiuffsdhiudhsifusdfhiufsdhiufsdhiusdfhiufsdhiufsdhiu())) {;
                        Client.getInstance().onStart();
                        Client.getInstance().setUsername(json.get("username").getAsString());
                        Client.getInstance().setDiscordTag(String.format("%s#%s", new JsonParser().parse(HttpUtil.get("https://sleekapi.realreset.repl.co/api/getdiscordinfo?id=" + json.get("discordID").getAsString())).getAsJsonObject().get("username"), new JsonParser().parse(HttpUtil.get("https://sleekapi.realreset.repl.co/api/getdiscordinfo?id=" + json.get("discordID").getAsString())).getAsJsonObject().get("discriminator")));
                        Client.getInstance().setRank(json.get("rank").getAsString());
                        mc.displayGuiScreen(new MainMenu());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
//            print ERROR
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void drawScreen(int x, int y2, float z) {
        final MCFontRenderer font = Fonts.Verdana;
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
        username.drawTextBox();
//        drawString(mc.fontRendererObj, "Client has been skidded by vncat", mc.fontRendererObj.getStringWidth("Client has been skidded by vncat") / 2, height - 60, ColorPalette.AMBER.getColor().getRGB());
        Fonts.Arial45.drawCenteredString("§lS", width / 2 - 24, height / 4 -24, ColorPalette.BLUE.getColor().getRGB());
        Fonts.Arial40.drawCenteredString("leek", width / 2 + 4, height / 4 -22.5f, -1); // -1 = white
        Fonts.Arial45.drawCenteredString("§lLog", width / 2 - 14, height / 4 -3.5f, -1); // -1 = white
        Fonts.Arial40.drawCenteredString("in", width / 2 + 18, height / 4 -2, ColorPalette.BLUE.getColor().getRGB());
        if (username.getText().isEmpty()) {
            font.drawStringWithShadow("UID", width / 2F - 96, 66 + 60, -7829368);
        }
        super.drawScreen(x, y2, z);
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        buttonList.add(new GuiButton(0, width / 2 - 100, 84 + 60, "Login to Sleek"));
        username = new GuiTextField(var3, mc.fontRendererObj, width / 2 - 100, 60 + 60, 200, 20);
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
