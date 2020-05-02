package ua.azbest;

import ua.azbest.model.Model;

public class App {

    public static void main(String[] args) {
        Model m = new Model(10);
        Thread model = new Thread(m);
        try {
            model.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        model.start();
    }

}
