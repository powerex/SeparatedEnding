package ua.azbest.machine;

import ua.azbest.comunication.Color;
import ua.azbest.model.Model;

public class ZeroMachine extends Machine {
    public ZeroMachine(double power, Model cluster) {
        super(power, cluster);
    }

    public boolean isOver() {
        return (color == Color.WHITE &&
                token != null &&
                token.getColor() == Color.WHITE &&
                token.getValue() == 0 &&
                baseCounter == 0
        );
    }
}