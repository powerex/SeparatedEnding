package ua.azbest.model;

import ua.azbest.machine.ZeroMachine;

public class Probe implements Runnable {

    private Model model;

    public Probe(Model model) {
        this.model = model;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        model.getZeroMachine().sendToken();
        //model.getMachines().stream().skip(1).forEach(m -> m.sendToken());
    }
}
