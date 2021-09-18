package me.bkrmt.bkcore.textanimator;

public enum AnimationType {
    SHINE("shine"),
    RGB_RAINBOW("rgb-rainbow");

    private final String message;

    AnimationType(String message) {
        this.message = message;
    }

    public String getString() {
        return message;
    }
}
