package me.bkrmt.bkcore.textanimator;

public class AnimationOptions {
    private final String text;
    private int speed;
    private int pause;
    private int size;

    protected AnimationOptions(String text, int speed, int pause, int size) {
        this.text = text;
        this.speed = speed;
        this.pause = pause;
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public int getSpeed() {
        return speed;
    }

    public int getPause() {
        return pause;
    }

    public int getSize() {
        return size;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setPause(int pause) {
        this.pause = pause;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
