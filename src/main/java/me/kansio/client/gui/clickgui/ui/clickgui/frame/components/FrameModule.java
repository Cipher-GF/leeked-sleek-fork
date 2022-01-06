package me.kansio.client.gui.clickgui.ui.clickgui.frame.components;


import me.kansio.client.Client;
import me.kansio.client.gui.clickgui.ui.clickgui.frame.Priority;
import me.kansio.client.gui.clickgui.ui.clickgui.frame.components.impl.BoolSetting;
import me.kansio.client.gui.clickgui.ui.clickgui.frame.components.impl.EnumSetting;
import me.kansio.client.gui.clickgui.ui.clickgui.frame.components.impl.SlideSetting;
import me.kansio.client.gui.clickgui.ui.clickgui.frame.components.impl.StringSetting;
import me.kansio.client.gui.clickgui.utils.render.RenderUtils;
import me.kansio.client.gui.clickgui.utils.render.animation.easings.Animate;
import me.kansio.client.gui.clickgui.utils.render.animation.easings.Easing;
import me.kansio.client.modules.impl.Module;
import me.kansio.client.property.value.BooleanValue;
import me.kansio.client.property.value.ModeValue;
import me.kansio.client.property.value.NumberValue;
import me.kansio.client.property.value.StringValue;
import me.kansio.client.utils.chat.ChatUtil;
import me.kansio.client.utils.render.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

public class FrameModule implements Priority {
    private final Module module;
    private final ArrayList<Component> components;

    private final FrameCategory owner;

    private final Animate moduleAnimation;

    private int x, y;
    private int offset;

    private boolean opened;
    private boolean listening;

    public FrameModule(Module module, FrameCategory owner, int x, int y) {
        this.module = module;
        this.components = new ArrayList<>();
        this.owner = owner;
        this.moduleAnimation = new Animate();
        moduleAnimation.setMin(0).setMax(255).setReversed(!module.isToggled()).setEase(Easing.LINEAR);
        this.opened = false;
        this.listening = false;
        this.x = x;
        this.y = y;

        /*/if(module.())
        {/*/


        Client.getInstance().getValueManager().getValuesFromOwner(module).forEach(setting ->
        {
            if (setting instanceof BooleanValue) {
                this.components.add(new BoolSetting(0, 0, this, setting));
            }
            if (setting instanceof ModeValue) {
                this.components.add(new EnumSetting(0, 0, this, setting));
            }
            if (setting instanceof NumberValue) {
                this.components.add(new SlideSetting(0, 0, this, setting));
            }
            if (setting instanceof StringValue) {
                this.components.add(new StringSetting(0, 0, this, setting));
            }
        });
        //}
    }

    public void drawScreen(int mouseX, int mouseY) {
        moduleAnimation.setReversed(!module.isToggled());
        moduleAnimation.setSpeed(1000).update();

        /*/if(GuiScreen.hover(x, y, mouseX, mouseY, defaultWidth, moduleHeight) && hoveredColor) {
            GuiScreen.drawRect(x,y, x + defaultWidth, y + moduleHeight, darkerMainColor);
        }/*/

        if (module.isToggled() || (moduleAnimation.isReversed() && moduleAnimation.getValue() != 0)) {
            GuiScreen.drawRect(x, y, x + defaultWidth, y + moduleHeight, ColorUtils.setAlpha(new Color(enabledColor), (int) moduleAnimation.getValue()).getRGB());
        }

        Minecraft.getMinecraft().fontRendererObj.drawString(listening ? "Press new keybind" : module.getName(), x + 3, y + (moduleHeight / 2F - (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2F)), stringColor, true);
        if (!module.getValues().isEmpty())
            Minecraft.getMinecraft().fontRendererObj.drawString(opened ? "-" : "+", (x + owner.getWidth()) - 9, y + (moduleHeight / 2F - (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT / 2F)), stringColor, true);

        int offset = 0;

        if (opened) {
            for (Component component : this.components) { // using for loop because continue isn't supported on foreach
                //component.getSetting().constantCheck();
                //if(component.getSetting().isHide()) continue;
                boolean shouldBeHidden;

                if (component.getSetting().hasParent() && component.getSetting().getParent() != null) {
                    if ((component.getSetting().getParent() instanceof ModeValue && ((ModeValue) component.getSetting().getParent()).getValue().equalsIgnoreCase(component.getSetting().getMode()))
                            || (component.getSetting().getParent() instanceof BooleanValue && ((BooleanValue) component.getSetting().getParent()).getValue())) {
                        shouldBeHidden = false;
                    } else {
                        shouldBeHidden = true;
                    }
                } else {
                    shouldBeHidden = false;
                }
                component.setHidden(shouldBeHidden);

                if (component.isHidden()) continue;
                component.setX(x);
                component.setY(y + moduleHeight + offset);

                component.drawScreen(mouseX, mouseY);

                offset += component.getOffset();
            }
        }

        this.setOffset(moduleHeight + offset);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (RenderUtils.hover(x, y, mouseX, mouseY, defaultWidth, moduleHeight) && RenderUtils.hover(owner.getX(), owner.getY(), mouseX, mouseY, defaultWidth, owner.getHeight())) {
            switch (mouseButton) {
                case 0:
                    module.toggle();
                    break;
                case 1:
                    opened = !opened;
                    break;
                case 2:
                    listening = true;
                    //TODO: Bind
                    break;
            }
            return true;
        }

        if (RenderUtils.hover(owner.getX(), owner.getY(), mouseX, mouseY, defaultWidth, owner.getHeight()) && opened) {
            for (Component component : this.components) {
                if (component.isHidden()) continue;
                if (component.mouseClicked(mouseX, mouseY, mouseButton))
                    return true;
            }
        }

        return false;
    }

    public int getOffset() {
        offset = 0;
        if (opened) {
            for (Component component : this.components) { // using for loop because continue isn't supported on foreach
                //component.getSetting().constantCheck();
                //if(component.getSetting().isHide()) continue;
                if (component.isHidden()) continue;

                offset += component.getOffset();
            }
        }

        this.setOffset(moduleHeight + offset);
        return offset;
    }


    public void keyTyped(char key, int keycode) {
        for (Component component : this.components) {
            if (component.isHidden()) continue;
            component.keyTyped(key, keycode);

        }

        if (listening) {
            if (keycode == Keyboard.KEY_ESCAPE) {
                module.setKeyBind(0);

            } else {
                module.setKeyBind(keycode);
                ChatUtil.log(module.getName() + " is now binded to " + Keyboard.getKeyName(keycode));
                listening = false;
            }
        }
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
