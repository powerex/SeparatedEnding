package ua.azbest.machine;

import ua.azbest.comunication.Color;
import ua.azbest.comunication.Token;
import ua.azbest.model.Model;

public class ZeroMachine extends Machine {

    private boolean tokenSend;

    public ZeroMachine(double power, Model cluster) {
        super(power, cluster);
        baseCounter.getAndSet(1);
        tokenSend = false;
    }

    public boolean isOver() {
        return (token != null &&
                token.getColor() == Color.WHITE &&
                token.getValue() == 0 &&
                cluster.sumBaseCounters() == 0
        );
    }

    @Override
    public void sendToken() {
        if (!tokenSend) {
            token = new Token();
            if (getCurrentState() instanceof ActiveMachine)
                token.setColor(Color.BLACK);
            token.steps.incrementAndGet();
            token.changeValue(getBaseCounter());
            cluster.getMachines().get(cluster.getMachines().size() - 1).receiveToken(token);
        }
    }

    @Override
    public void receiveToken(Token token) {
        tokenSend = false;
        this.token = token;

        //getCluster().noticeTokenSend(getId());

        if (isOver() && getCurrentState() instanceof PassiveMachine)
            cluster.setActive(false);
    }
}