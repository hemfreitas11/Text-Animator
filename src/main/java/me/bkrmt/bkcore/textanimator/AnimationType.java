package me.bkrmt.bkcore.textanimator;

public enum AnimationType {
    SHINE("shine");

    private final String message;

    AnimationType(String message) {
        this.message = message;
    }

    public String getString() {
        return message;
    }
}
