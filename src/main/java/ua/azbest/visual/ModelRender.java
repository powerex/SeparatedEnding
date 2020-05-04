package ua.azbest.visual;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ua.azbest.comunication.Token;
import ua.azbest.machine.ActiveMachine;
import ua.azbest.model.Model;

public class ModelRender {

    private final Model model;
    private final GraphicsContext gc;

    private final double W = 50;
    private final double H = W * 1.62;
    double innerRadius;
    double centerX;
    double centerY;

    public ModelRender(Model model, GraphicsContext gc) {
        this.model = model;
        this.gc = gc;
        centerX = gc.getCanvas().getWidth() / 2;
        centerY = gc.getCanvas().getHeight() / 2;
        innerRadius = gc.getCanvas().getWidth() * 0.4;
    }

    public void drawMachines() {
        gc.setFill(Color.GREY);

        for (int i = 0; i < model.getClusterSize(); i++) {
            drawMachine(i);
        }
    }

    public void drawMachineActive(int i) {
        drawMachine(i, true);
    }

    public void drawMachinePassive(int i) {
        drawMachine(i, false);
    }

    private void drawMachine(int i) {
        drawMachine(i, false);
    }

    private void drawMachine(int i, boolean active) {
        if (active) {
            gc.setFill(Color.FIREBRICK);
            gc.setStroke(Color.WHITE);
        } else {
            gc.setFill(Color.LIGHTGRAY);
            gc.setStroke(Color.BLACK);
        }
        double phi = getPhi(i);
        double shiftX = Math.cos(phi) * innerRadius + centerX - W / 2;
        double shiftY = Math.sin(phi) * innerRadius + centerY - H / 2;
        gc.fillRect(shiftX, shiftY, W, H);
        gc.strokeText(String.valueOf(i), shiftX + 5, shiftY + 15);
    }

    private double getPhi(int i) {
        return ((double)i * 360 / model.getClusterSize() - 90) * Math.PI / 180;
    }


    public void drawToken(Token token, int i) {

        double phi = getPhi(i);
        double shiftX = Math.cos(phi) * innerRadius * 0.6 + centerX;
        double shiftY = Math.sin(phi) * innerRadius * 0.6 + centerY;

        gc.setFill(Color.MINTCREAM);
        gc.fillOval(centerX - innerRadius * 0.8,centerY - innerRadius * 0.8,innerRadius * 1.6, innerRadius * 1.6);

        switch (token.getColor()) {
            case WHITE:
                gc.setFill(Color.YELLOW);
                break;
            case BLACK:
                gc.setFill(Color.DARKGRAY);
                break;
        }

        gc.fillOval(shiftX, shiftY, W / 4, W / 4);

        gc.setStroke(Color.BLUEVIOLET);
        gc.strokeText(String.valueOf(token.getId()) + '(' + token.steps.incrementAndGet() + ')', shiftX, shiftY);
    }

}
