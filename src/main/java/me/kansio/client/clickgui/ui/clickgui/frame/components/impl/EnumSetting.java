package me.kansio.client.clickgui.ui.clickgui.frame.components.impl;

import me.kansio.client.clickgui.utils.render.RenderUtils;
import me.kansio.client.clickgui.ui.clickgui.frame.Priority;
import me.kansio.client.clickgui.ui.clickgui.frame.components.Component;
import me.kansio.client.clickgui.ui.clickgui.frame.components.FrameModule;
import me.kansio.client.property.Value;
import me.kansio.client.property.value.ModeValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class EnumSetting extends Component implements Priority {
    public EnumSetting(int x, int y, FrameModule owner, Value setting) {
        super(x, y, owner, setting);
    }

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
        fontRenderer.drawString(getSetting().getName(), x + 5, y + (getOffset() / 2F - (fontRenderer.FONT_HEIGHT / 2F)), -1, true);
        fontRenderer.drawString(((ModeValue) getSetting()).getValue().toUpperCase(), x + defaultWidth - fontRenderer.getStringWidth(((ModeValue) getSetting()).getValue().toUpperCase()) - 5, y + (getOffset() / 2F - (fontRenderer.FONT_HEIGHT / 2F)), -1, true);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(RenderUtils.hover(x, y, mouseX, mouseY, defaultWidth, getOffset()))
        {
            ModeValue enumValue = (ModeValue) getSetting();

            int enumIndex = 0;
            for(String _enum : enumValue.getChoices()) {
                if(_enum == enumValue.getValue()) break;
                ++enumIndex;
            }

            if(mouseButton == 1) {
                if(enumIndex - 1 >= 0) {
                    enumValue.setValue(enumValue.getChoices().get(enumIndex - 1));
                } else {
                    enumValue.setValue(enumValue.getChoices().get(enumValue.getChoices().size() - 1));
                }
            }

            if(mouseButton == 0) {
                if(enumIndex + 1 < enumValue.getChoices().size()) {
                    enumValue.setValue(enumValue.getChoices().get(enumIndex + 1));
                } else {
                    enumValue.setValue(enumValue.getChoices().get(0));
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onGuiClosed(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public int getOffset() {
        return 15;
    }
}
