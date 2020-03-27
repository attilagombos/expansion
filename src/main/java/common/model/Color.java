package common.model;

import static java.util.Arrays.stream;

public enum Color {
    RED, GREEN, BLUE, PURPLE;

    public static Color ofOrdinal(int ordinal) {
        return stream(Color.values()).filter(color -> color.ordinal() == ordinal).findFirst().orElse(null);
    }
}
