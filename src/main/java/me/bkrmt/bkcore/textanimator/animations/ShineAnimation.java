package me.bkrmt.bkcore.textanimator.animations;

import me.bkrmt.bkcore.textanimator.AnimationResponse;
import me.bkrmt.bkcore.textanimator.TextAnimation;
import me.bkrmt.bkcore.textanimator.TextAnimator;
import org.bukkit.ChatColor;

import static org.bukkit.ChatColor.COLOR_CHAR;

public class ShineAnimation implements TextAnimation {
    private final String color;
    private final boolean bold;
    private final String pausedFrame;

    public ShineAnimation(String color, boolean bold, String pausedFrame) {
        this.color = color;
        this.bold = bold;
        this.pausedFrame = pausedFrame;
    }

    @Override
    public AnimationResponse processText(TextAnimator animator, int index, int currentPause) {
        StringBuilder frame = new StringBuilder(ChatColor.stripColor(animator.getInitialText()));
        int size = animator.getSize();
        if (frame.length() == size-1) size = size-2;
        if (frame.length() == size) size--;
        if (index > frame.length() + size) {
            if (currentPause < animator.getPauseAmount()) {
                frame.replace(0, frame.length(), getPausedFrame());
                currentPause++;
            } else {
                index = 0;
                currentPause = 0;
            }
        }
        if (currentPause == 0) {
            if (index < frame.length()) {
                char leadChar = frame.charAt(index);
                if (index + 1 < frame.length()) 
                    frame.replace(index, index + 1, COLOR_CHAR + "f" + (bold ? COLOR_CHAR + "l" : "") + leadChar + getColor());
                else 
                    frame = new StringBuilder(frame.substring(0, frame.length() - 1) + COLOR_CHAR + "f" + (bold ? COLOR_CHAR + "l" : "") + leadChar);
                if (index <= size) 
                    frame.replace(0, index, COLOR_CHAR + "f" + (bold ? COLOR_CHAR + "l" : "") + ChatColor.stripColor(animator.getInitialText()).substring(0, index));
            }
            if (index > size) 
                frame.replace(0, (index - size),
                        getColor() + ChatColor.stripColor(animator.getInitialText()).substring(0, (index - size)) + COLOR_CHAR + "f" + (bold ? COLOR_CHAR + "l" : ""));
            index++;
        }
        return new AnimationResponse(index, currentPause, frame.toString());
    }

    public String getColor() {
        return color;
    }

    public boolean isBold() {
        return bold;
    }

    public String getPausedFrame() {
        return pausedFrame;
    }
}
