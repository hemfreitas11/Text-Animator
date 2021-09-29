package me.bkrmt.bkcore.textanimator.animations.rgb;

import me.bkrmt.bkcore.iridium.IridiumColorAPI;
import me.bkrmt.bkcore.textanimator.AnimationResponse;
import me.bkrmt.bkcore.textanimator.TextAnimation;
import me.bkrmt.bkcore.textanimator.TextAnimator;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;

public class RGBRainbowAnimation implements TextAnimation {
    private AlternatingColor startAlternator;
    private AlternatingColor endAlternator;
    private String initialText;

    @Override
    public AnimationResponse processText(TextAnimator animator, int index, int currentPause) {
        if (initialText == null) {
            initialText = ChatColor.stripColor(animator.getInitialText());
        }
        if (startAlternator == null) {
            startAlternator = new AlternatingColor(getRandomColor(), getRandomColor(), 9);
        }
        if (endAlternator == null) {
            endAlternator = new AlternatingColor(getRandomColor(), getRandomColor(), 9);
        }
        if (startAlternator.getCurrent() >= startAlternator.getColorsSize()) {
            startAlternator = new AlternatingColor(startAlternator.getEnd(), getRandomColor(), 9);
        }
        if (endAlternator.getCurrent() >= endAlternator.getColorsSize()) {
            endAlternator = new AlternatingColor(endAlternator.getEnd(), getRandomColor(), 9);
        }
        Color startColor = startAlternator.getNextColorStep();
        Color endColor = endAlternator.getNextColorStep();
        return new AnimationResponse(index, currentPause, IridiumColorAPI.color(initialText, startColor, endColor));
    }

    public int getRandomColor() {
        return (int) ((Math.random() * (16777216 - 0)) + 0);
    }
}

class AlternatingColor {
    private final int start;
    private final int end;
    private final int step;
    private final Color[] colors;
    private int current;

    public AlternatingColor(int start, int end, int step) {
        this.start = start;
        this.end = end;
        this.step = step;
        this.colors = IridiumColorAPI.createGradientColors(new Color(start), new Color(end), step);
        this.current = 0;
    }

    public Color getNextColorStep() {
        return colors[current++];
    }

    public int getColorsSize() {
        return colors.length;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getStep() {
        return step;
    }

    public int getCurrent() {
        return current;
    }
}
