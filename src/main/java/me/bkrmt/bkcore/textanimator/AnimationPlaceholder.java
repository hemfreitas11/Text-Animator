package me.bkrmt.bkcore.textanimator;

import org.bukkit.ChatColor;

public class AnimationPlaceholder {
    private final String color;
    private final boolean bold;
    private final AnimationType animationType;
    private final AnimationOptions options;
    private final String cleanText;
    private final String rawText;

    protected AnimationPlaceholder(String rawText, String color, boolean bold, AnimationType animationType, AnimationOptions options) {
        this.rawText = rawText;
        this.cleanText = (AnimatorManager.cleanText(rawText)).trim();
        this.color = color;
        this.bold = bold;
        this.animationType = animationType;
        this.options = options;
    }

    public String getCleanText() {
        return (cleanText == null ? "" : cleanText);
    }

    public String getColor() {
        return color;
    }

    public boolean isBold() {
        return bold;
    }

    public String getRawText() {
        return rawText;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public AnimationOptions getOptions() {
        return options;
    }

    public String getColoredCleanText() {
        return (getColor() != null ? getColor() : "") + (isBold() ? (ChatColor.COLOR_CHAR + "l") : "") + getCleanText();
    }

    public void print() {
        System.out.println(getAnimationType());
        System.out.println(getRawText());
        System.out.println(getCleanText());
        System.out.println(getColoredCleanText());
        System.out.println(isBold());
        System.out.println(getColor());
        System.out.println(getOptions().getText());
        System.out.println(getOptions().getSize());
        System.out.println(getOptions().getPause());
        System.out.println(getOptions().getSpeed());
    }
}
