package today.sleek.client.rank;

import net.minecraft.util.EnumChatFormatting;

public enum UserRank {
    DEVELOPER("Developer", EnumChatFormatting.GOLD), BETA("Beta", EnumChatFormatting.LIGHT_PURPLE), USER("Normal", EnumChatFormatting.GRAY);
    private String displayName;
    private EnumChatFormatting color;

    UserRank(String name, EnumChatFormatting color) {
        this.displayName = name;
        this.color = color;
    }

    @SuppressWarnings("all")
    public String getDisplayName() {
        return this.displayName;
    }

    @SuppressWarnings("all")
    public EnumChatFormatting getColor() {
        return this.color;
    }
}
