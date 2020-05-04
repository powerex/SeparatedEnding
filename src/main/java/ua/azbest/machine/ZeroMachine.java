package ua.azbest.machine;

import ua.azbest.comunication.Color;
import ua.azbest.comunication.Token;
import ua.azbest.model.Model;

public class ZeroMachine extends Machine {

    public ZeroMachine(double power, Model cluster) {
        super(power, cluster);
        baseCounter.getAndSet(1);
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
        if (!cluster.isTokenSend()) {
            token = new Token();
            if (getCurrentState() instanceof ActiveMachine)
                token.setColor(Color.BLACK);
            token.steps.incrementAndGet();
            token.changeValue(getBaseCounter());
            cluster.getMachines().get(cluster.getMachines().size() - 1).receiveToken(token);
            cluster.setTokenSend(true);
        } else {
            //System.out.println("Already sent");
        }
    }

    @Override
    public void receiveToken(Token token) {
        this.token = token;
        if (getCurrentState() instanceof PassiveMachine)
            cluster.setTokenSend(false);
        //System.err.println("tokenSend set to false!!!");
        //System.err.println("From cluster " + cluster.isActive());
        //if (cluster.isTokenSend()) System.out.println("TRUE"); else System.out.println("FALSE");
        getCluster().noticeTokenSend(getId());

        if (isOver() && getCurrentState() instanceof PassiveMachine)
            cluster.setActive(false);
    }
}