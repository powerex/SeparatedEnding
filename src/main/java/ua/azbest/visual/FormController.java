package ua.azbest.visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import ua.azbest.model.Model;
import ua.azbest.model.Statistic;
import ua.azbest.model.VisualModel;

import java.io.PrintStream;

public class FormController {

    @FXML
    Button startModelingButton;

    @FXML
    Button statButton;
    @FXML
    Canvas canvas;
    @FXML
    TextField taskLeft;
    @FXML
    TableView table;
    @FXML
    TableColumn columnId;
    @FXML
    TableColumn columnOn;
    @FXML
    TableColumn columnTime;
    @FXML
    TableColumn columnPercents;

//    private PrintStream ps = new PrintStream(new Console(console));

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
//        model.setPrintStream(ps);

        gc.setFill(Color.MINTCREAM);
        gc.fillRect(0,0,gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        render.drawMachines();

        columnId.setCellValueFactory(new PropertyValueFactory<Statistic, String>("id"));
        columnOn.setCellValueFactory(new PropertyValueFactory<Statistic, String>("on"));
        columnTime.setCellValueFactory(new PropertyValueFactory<Statistic, String>("time"));
        columnTime.setStyle( "-fx-alignment: CENTER-RIGHT;");
        columnPercents.setCellValueFactory(new PropertyValueFactory<Statistic, String>("percents"));
        columnPercents.setStyle( "-fx-alignment: CENTER-RIGHT;");
    }


    public void fillStat(ActionEvent actionEvent) {
        table.getItems().addAll(model.getStatistics());
    }
}
