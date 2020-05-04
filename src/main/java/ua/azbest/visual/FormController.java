package ua.azbest.visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import ua.azbest.model.Model;
import ua.azbest.model.VisualModel;

public class FormController {

    @FXML
    Button startModelingButton;
    @FXML
    Canvas canvas;

    GraphicsContext gc;
    ModelRender render;
    VisualModel model;

    public void startModeling(ActionEvent actionEvent) {

        Thread modelThread = new Thread(model);
        try {
            modelThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        modelThread.start();

    }

    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        model = new VisualModel(10);
        render = new ModelRender(model, gc);
        model.setRender(render);

        gc.setFill(Color.MINTCREAM);
        gc.fillRect(0,0,gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        render.drawMachines();

    }
}
