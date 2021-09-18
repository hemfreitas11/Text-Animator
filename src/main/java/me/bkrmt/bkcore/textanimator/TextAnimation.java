package me.bkrmt.bkcore.textanimator;

public interface TextAnimation {
    AnimationResponse processText(TextAnimator animator, int index, int currentPause);
}
