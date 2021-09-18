package me.bkrmt.bkcore.textanimator;

public class AnimationResponse {
    private final int newIndex;
    private final int newCurrentPause;
    private final String processedText;

    public AnimationResponse(int newIndex, int newCurrentPause, String processedText) {
        this.newIndex = newIndex;
        this.newCurrentPause = newCurrentPause;
        this.processedText = processedText;
    }

    public int getNewIndex() {
        return newIndex;
    }

    public int getNewCurrentPause() {
        return newCurrentPause;
    }

    public String getProcessedText() {
        return processedText;
    }
}
