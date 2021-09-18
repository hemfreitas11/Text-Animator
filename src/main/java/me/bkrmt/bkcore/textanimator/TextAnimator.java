package me.bkrmt.bkcore.textanimator;

import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.ChatColor.COLOR_CHAR;

public class TextAnimator {
    private final JavaPlugin plugin;
    private final String initialText;
    private AnimationReceiver receiver;
    private CancellableTask animationTask;
    private TextAnimation animation;
    private final String identifier;
    private int size = 4;
    private int pauseAmount = 0;
    private int updateFrequency = 5;
    int index = 0;
    int currentPause = 0;

    public TextAnimator(JavaPlugin plugin, String identifier, String initialText) {
        this.plugin = plugin;
        this.identifier = identifier;
        this.initialText = initialText;
    }

    public TextAnimator animate() {
        TextAnimator animator = this;
        if (animationTask == null) {
            animationTask = new CancellableTask(
                    () -> {
                        AnimationResponse response = getAnimation().processText(animator, index, currentPause);
                        setIndex(response.getNewIndex());
                        setCurrentPause(response.getNewCurrentPause());
                        if (receiver != null && animation != null) {
//                    System.out.println(identifier);
                            getReceiver().sendAnimationFrame(COLOR_CHAR + "r" + response.getProcessedText() + COLOR_CHAR + "r");
                        } else {
                            if (!animationTask.isCancelled()) animationTask.cancel();
                        }
                    },
                    runnable -> runnable.runTaskTimerAsynchronously(plugin, 0, getUpdateFrequency())
            );
        }
        animationTask.start();
        return this;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setAnimationTask(CancellableTask animationTask) {
        this.animationTask = animationTask;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public String getInitialText() {
        return initialText;
    }

    public CancellableTask getAnimationTask() {
        return animationTask;
    }

    public AnimationReceiver getReceiver() {
        return receiver;
    }

    public TextAnimator setReceiver(AnimationReceiver receiver) {
        this.receiver = receiver;
        return this;
    }

    public TextAnimation getAnimation() {
        return animation;
    }

    public TextAnimator setAnimation(TextAnimation animation) {
        this.animation = animation;
        return this;
    }

    public int getSize() {
        return size;
    }

    public TextAnimator setSize(int size) {
        this.size = size;
        return this;
    }

    public int getPauseAmount() {
        return pauseAmount;
    }

    public TextAnimator setPauseAmount(int pauseAmount) {
        this.pauseAmount = pauseAmount;
        return this;
    }

    public int getUpdateFrequency() {
        return updateFrequency;
    }

    public TextAnimator setUpdateFrequency(int updateFrequency) {
        this.updateFrequency = updateFrequency;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public TextAnimator setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getCurrentPause() {
        return currentPause;
    }

    public TextAnimator setCurrentPause(int currentPause) {
        this.currentPause = currentPause;
        return this;
    }

    public TextAnimator pause() {
        if (animationTask != null)
            animationTask.cancel();
        return this;
    }

    TextAnimator destroy() {
        if (getAnimationTask() != null) {
            getAnimationTask().cancel();
            animationTask = null;
        }
        receiver = null;
        return this;
    }
}