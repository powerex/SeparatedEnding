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
    public void sendMessage(Message message) {

    }

    @Override
    public void receiveToken(Token token) {

    }

    @Override
    public void sendToken(Token token) {

    }
}
