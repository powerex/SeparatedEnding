package ua.azbest.comunication;

import java.util.concurrent.atomic.AtomicInteger;

public class Token {

    //for debug
    public AtomicInteger steps;

    private int value;
    private Color color;

    public Token() {
        steps = new AtomicInteger(0);
        value = 0;
        color = Color.WHITE;
    }

    public Color getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void changeValue(int delta) {
        this.value += delta;
    }

    @Override
    public String toString() {
        return "Token{" +
                "steps=" + steps.get() +
                ", value=" + value +
                ", color=" + color +
                '}';
    }
}
