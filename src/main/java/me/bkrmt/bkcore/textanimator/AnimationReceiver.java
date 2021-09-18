package me.bkrmt.bkcore.textanimator;

@FunctionalInterface
public interface AnimationReceiver {
    void sendAnimationFrame(String frame);
}
