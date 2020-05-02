package ua.azbest.comunication;

public class Token {

    private int value;
    private Color color;

    public Token() {
        value = 0;
        color = Color.WHITE;
    }

    public Color getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }
}
