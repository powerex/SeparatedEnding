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
        machine.decreaseBaseCounter();
        message.setSourceId(machine.getId());
        sendMessage(message);
    }

    @Override
    public void sendMessage(Message message) {
        machine.increaseBaseCounter();
        machine.getCluster().getChannel().push(message);
        System.out.println(machine.getId() + ": Sum base count:::::: -> " + machine.getCluster().getMachines().stream().mapToInt(m -> m.getBaseCounter()).sum());
    }

    @Override
    public void receiveToken(Token token) {
        // do nothing, just overwrite prev token
    }

    @Override
    public void sendToken() throws UnreachableStateException {
        throw new UnreachableStateException("invoke sendToken() on ActiveMachine state");
    }
}
