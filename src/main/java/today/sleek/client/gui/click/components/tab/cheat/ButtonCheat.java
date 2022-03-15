package today.sleek.client.gui.click.components.tab.cheat;

import java.awt.Color;
import org.lwjgl.input.*;
import today.sleek.client.gui.click.Interface;
import today.sleek.client.gui.click.components.base.BaseButton;
import today.sleek.client.gui.click.tab.cheat.TabDefaultCheat;
import today.sleek.client.gui.click.utils.Draw;
import today.sleek.client.modules.impl.Module;
import today.sleek.client.utils.font.Fonts;
import today.sleek.client.utils.math.Stopwatch;

public class ButtonCheat extends BaseButton
{
    private TabDefaultCheat parentTab;
    private ContainerCheats parentContainer;
    private Module cheat;
    private Stopwatch stopwatch;
    private int opacityOffset;
    private boolean listeningForKey;
    
    public ButtonCheat(final Interface theInterface, final TabDefaultCheat parentTab, final ContainerCheats parentContainer, final Module cheat, final double x, final double y, final double width, final double height, final Action action) {
        super(theInterface, x, y, width, height, action);
        this.stopwatch = new Stopwatch();
        this.opacityOffset = 0;
        this.listeningForKey = false;
        this.parentTab = parentTab;
        this.parentContainer = parentContainer;
        this.cheat = cheat;
    }

    int animation;
    boolean s;
    int prev_anim;
    
    @Override
    public void drawComponent(final double x, final double y) {
        this.positionX = x - this.theInterface.getPositionX();
        this.positionY = y - this.theInterface.getPositionY();
        if (this.stopwatch.timeElapsed(50)) {
            if (this.isMouseOver()) {
                if (this.opacityOffset < 80) {
                    this.opacityOffset += 16;
                }
            }
            else if (this.opacityOffset > 0) {
                this.opacityOffset -= 16;
            }
            this.stopwatch.resetTime();
        }
        Color color;
        if (!(parentTab.getSelectedCheat() == this.cheat)) {
            color = new Color(18, 17, 18);
        } else {
            color = new Color(20, 19, 20);
        }
        if (cheat.isToggled()) {
            Draw.drawRectangle(x, y - 1, x + this.maxWidth, y + 1 + this.maxHeight, new Color(40, 40, 40).getRGB());
            if (!this.listeningForKey) {
                Fonts.Arial15.drawString("    " + this.cheat.getName(), x + 8, y + 9, Color.WHITE.getRGB());
            } else {
                Fonts.Arial15.drawString("    " + "...", x + 8, y + 9, Color.WHITE.getRGB());
            }
            Fonts.Arial30.drawString(" �a�", x + 2.0, y + 6.5, Color.WHITE.getRGB());
        } else {
            if (!this.listeningForKey) {
                Fonts.Arial15.drawString("    " + this.cheat.getName(), x + 8, y + 9, Color.WHITE.getRGB());
            } else {
                Fonts.Arial15.drawString("    " + "...", x + 8, y + 9, Color.WHITE.getRGB());
            }
            Fonts.Arial30.drawString(" �c�", x + 2.0, y + 6.5, Color.WHITE.getRGB());
        }
        //String extraInfo = "";
        if (this.cheat.getValues().isEmpty()) {
            //extraInfo = "No options";
        }
        else {
            //extraInfo = String.valueOf(this.cheat.getPropertyRegistry().size()) + ((this.cheat.getPropertyRegistry().size() > 1) ? " options" : " option");
        }
        //Fonts.Arial12.drawString(extraInfo, x + 5.0, y + 17.0, this.theInterface.getColor(100, 100, 100));
        /*/Draw.drawRectangle(x + this.maxWidth - 25.0, y + 4.0, x + this.maxWidth - 5.0, y + 12.0, new Color(0, 0, 0, 196).getRGB());
        if (this.cheat.getState()) {
            Draw.drawRectangle(x + this.maxWidth - 14.0, y + 5.0, x + this.maxWidth - 6.0, y + 11.0, new Color(80, 150, 80).getRGB());
        }
        else {
            Draw.drawRectangle(x + this.maxWidth - 24.0, y + 5.0, x + this.maxWidth - 16.0, y + 11.0, new Color(200, 80, 80).getRGB());
        }/*/

        if (this.theInterface.isMouseInBounds(this.theInterface.getPositionX() + this.positionX, this.theInterface.getPositionX() + this.positionX + this.maxWidth, this.theInterface.getPositionY() + this.positionY, this.theInterface.getPositionY() + this.positionY + this.maxHeight)) {
          //  if (!s) {
          //      animation = 0;
           // }
           // if (animation < 255) {
           //     animation = animation + 5;
           //     s = true;
           // }

            //if (animation > 255) {
             //   animation = 255;
             //   s = true;
           // }


            //ChatUtil.chat(animation + " up");
            //String bindText = (this.cheat.getBind() != 0) ? Keyboard.getKeyName(this.cheat.getBind()) : " ";
            //if ((bindText.equals(" "))) {
               // Fonts.f14.drawString("[" + bindText + "]", x + this.maxWidth - 5.0 - Fonts.f14.getStringWidth("[" + bindText + "]"), y + 9.5, new Color(255, 255, 255, animation).getRGB());
            //}
            //Fonts.Arial12.drawString("�o" + cheat.getDescription() + "", x + 5.0, y + 17.0, new Color(255, 255, 255, animation).getRGB());
            //prev_anim = animation;
        } else {
            //s = false;
        }

        //if (this.listeningForKey) {
          //  bindText = "...";
        //}
        //Fonts.f14.drawString("Bind [" + bindText + "]", x + this.maxWidth - 5.0 - Fonts.f14.getStringWidth("Bind [" + bindText + "]"), y + 16.5, Color.WHITE.getRGB());
    }
    
    @Override
    public boolean mouseButtonClicked(final int button) {
        String bindText = (this.cheat.getKeyBind() != 0) ? Keyboard.getKeyName(this.cheat.getKeyBind()) : "  ";
        if (this.listeningForKey) {
            bindText = "...";
        }
        /*/if (this.theInterface.isMouseInBounds(this.theInterface.getPositionX() + this.positionX + this.maxWidth - 25.0, this.theInterface.getPositionX() + this.positionX + this.maxWidth - 5.0, this.theInterface.getPositionY() + this.positionY + 4.0, this.theInterface.getPositionY() + this.positionY + 12.0)) {
            this.cheat.setState(!this.cheat.getState(), true);
            return true;
        }
        if (this.theInterface.isMouseInBounds(this.theInterface.getPositionX() + this.positionX + this.maxWidth - 7.0 - Fonts.f14.getStringWidth("[" + bindText + "]"), this.theInterface.getPositionX() + this.positionX + this.maxWidth - 3.0, this.theInterface.getPositionY() + this.positionY + 14.5, this.theInterface.getPositionY() + this.positionY + 24.0)) {
            return this.listeningForKey = true;
        }/*/
        if (this.theInterface.isMouseInBounds(this.theInterface.getPositionX() + this.positionX, this.theInterface.getPositionX() + this.positionX + this.maxWidth, this.theInterface.getPositionY() + this.positionY, this.theInterface.getPositionY() + this.positionY + this.maxHeight)) {
            //if (!this.cheat.getPropertyRegistry().isEmpty()) {
            switch (button) {
                case 0: {
                    //ChatUtil.chat("1");
                    this.parentTab.setSelectedCheat(this.cheat);
                    break;
                }

                case 1: {
                    //ChatUtil.chat("2");
                    this.cheat.setToggled(!this.cheat.isToggled());
                    break;
                }

                case 2: {
                    if (!listeningForKey) {
                        this.listeningForKey = true;
                    } else {
                        this.listeningForKey = false;
                    }
                    //ChatUtil.chat("3");
                    break;
                }
            }
                //if (button == 1) {
                    //this.parentTab.setSelectedCheat(this.cheat);
                //}
            //}
            return true;
        }
        return false;
    }
    
    @Override
    public boolean keyTyped(final char typedChar, final int keyCode) {
        if (this.listeningForKey) {
            if (keyCode == 1) {
                this.cheat.setKeyBind(0);
                this.listeningForKey = false;
            }
            else {
                this.cheat.setKeyBind(keyCode);
                this.listeningForKey = false;
            }
            return true;
        }
        return false;
    }
}
