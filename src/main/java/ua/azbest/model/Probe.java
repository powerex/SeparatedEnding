package ua.azbest.model;

import ua.azbest.machine.ZeroMachine;

public class Probe implements Runnable {

    private Model model;

    public Probe(Model model) {
        this.model = model;
    }

    @Override
    public void run() {
        while (model.isActive()) {
            //System.out.println("From probe: " + model.isTokenSend());
            model.getZeroMachine().sendToken();
            try {
                Thread.sleep(1300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
