package ua.azbest.machine;

import ua.azbest.comunication.Message;
import ua.azbest.comunication.Token;

public class PassiveMachine implements MachineState  {

    private Machine machine;

    public PassiveMachine(Machine machine) {
        this.machine = machine;
    }

    @Override
    public void receiveMessage(Message message) {
        machine.decreaseBaseCounter();
        machine.setTaskSize(message.getTaskSize());
        machine.setCurrentState(new ActiveMachine(machine));
        machine.increaseCountOn();
        machine.setStartTimeWorking(System.currentTimeMillis());
        Thread th = new Thread(machine);
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        th.setDaemon(true);
        th.start();
    }

    @Override
    public void sendMessage(Message message) throws UnreachableStateException {
        throw new UnreachableStateException("invoked sendMessage() from PassiveMachine state");
    }

    @Override
    public void receiveToken(Token token) {
        System.err.println(" -> -> -> forward token to " + machine.getCluster().getMachines().get(machine.getId()-1));
        sendToken();
    }

    @Override
    public void sendToken() {
        machine.getToken().steps.incrementAndGet();

//        System.out.println("Sum base count:::::: -> " + machine.getCluster().getMachines().stream().mapToInt(m -> m.getBaseCounter()).sum());

        machine.token.changeValue(machine.getBaseCounter());
        System.err.println("Send token, value: " + machine.getToken() + " --------------------------- From Machine: " + machine.getId());
        machine.getCluster().getMachines().get(machine.getId()-1).receiveToken(machine.token);
        machine.setToken(null);
    }
}
