package ua.azbest.machine;

import ua.azbest.comunication.Message;
import ua.azbest.comunication.Token;

public class PassiveMachine implements MachineState  {

    private Machine machine;

    public PassiveMachine(Machine machine) {
        this.machine = machine;
        if (machine.getCluster() != null)
            machine.getCluster().noticeMachineSetPassive(machine.getId());
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
        sendToken();
    }

    @Override
    public void sendToken() {
        machine.getToken().steps.incrementAndGet();
        machine.token.changeValue(machine.getBaseCounter());
        machine.getCluster().getMachines().get(machine.getId()-1).receiveToken(machine.token);
        machine.setToken(null);
    }
}
