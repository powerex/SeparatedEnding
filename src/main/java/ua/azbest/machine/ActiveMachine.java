package ua.azbest.machine;

import ua.azbest.comunication.Message;
import ua.azbest.comunication.Token;

public class ActiveMachine implements MachineState {

    private Machine machine;

    public ActiveMachine(Machine machine) {
        this.machine = machine;
    }

    @Override
    public void receiveMessage(Message message) {
        message.setSourceId(machine.getId());
        machine.getCluster().getChannel().push(message);
    }

    @Override
    public void sendMessage(Message message) {
        machine.increaseBaseCounter();
        machine.getCluster().getChannel().push(message);
    }

    @Override
    public void receiveToken(Token token) {

    }

    @Override
    public void sendToken(Token token) {

    }
}
