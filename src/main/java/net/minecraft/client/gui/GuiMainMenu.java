package net.minecraft.client.gui;

import java.util.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.kansio.client.Client;
import me.kansio.client.gui.MainMenu;
import me.kansio.client.utils.font.Fonts;
import me.kansio.client.utils.font.MCFontRenderer;
import me.kansio.client.utils.network.HttpUtil;
import negroidslayer.NegroidFarm;
import org.lwjgl.input.Keyboard;
import java.io.IOException;

public class GuiMainMenu extends GuiScreen {
    private GuiTextField username;


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
//            print ERROR
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void drawScreen(int x, int y2, float z) {
        final MCFontRenderer font = Fonts.Verdana;
        drawDefaultBackground();
        username.drawTextBox();
//        drawString(mc.fontRendererObj, "Client has been skidded by vncat", mc.fontRendererObj.getStringWidth("Client has been skidded by vncat") / 2, height - 60, ColorPalette.AMBER.getColor().getRGB());
        font.drawCenteredString("Login", (int) (width / 2F), 20 + 60, -1);
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
