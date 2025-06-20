package cc.chipchop.entity;

public enum Role {
    USER(1),
    ADMIN(2);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
