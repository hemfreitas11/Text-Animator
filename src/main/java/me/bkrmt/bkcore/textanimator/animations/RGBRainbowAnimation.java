package me.bkrmt.bkcore.textanimator.animations;

import me.bkrmt.bkcore.textanimator.AnimationResponse;
import me.bkrmt.bkcore.textanimator.TextAnimation;
import me.bkrmt.bkcore.textanimator.TextAnimator;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;

public class RGBRainbowAnimation implements TextAnimation {
    private final boolean bold;
    private final String pausedFrame;

    public RGBRainbowAnimation (boolean bold, String pausedFrame) {
        this.bold = bold;
        this.pausedFrame = pausedFrame;
    }

    @Override
    public AnimationResponse processText(TextAnimator animator, int index, int currentPause) {
        System.out.println(animator.getUpdateFrequency());
        StringBuilder frame = new StringBuilder(ChatColor.of(new Color(index)) + ChatColor.stripColor(animator.getInitialText()));
        if (index >= 2000) index = 0;
        else index++;
        return new AnimationResponse(index, currentPause, frame.toString());
    }

    public boolean isBold() {
        return bold;
    }

    public String getPausedFrame() {
        return pausedFrame;
    }
}
