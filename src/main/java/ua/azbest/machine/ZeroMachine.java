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
        return (color == Color.WHITE &&
                token != null &&
                token.getColor() == Color.WHITE &&
                token.getValue() == 0 &&
                baseCounter.get() == 0
        );
    }

    @Override
    public void sendToken() {
        token = new Token();
        if (getCurrentState() instanceof ActiveMachine)
            token.setColor(Color.BLACK);
        token.steps.incrementAndGet();
        token.changeValue(getBaseCounter());
        System.out.println("Zero send token:" + token);
        cluster.getMachines().get(cluster.getMachines().size()-1).receiveToken(token);
    }

    @Override
    public void receiveToken(Token token) {
        System.out.println(token);
        /*this.token = token;
        if (isOver())
            System.out.println(" ! ~~~~ Over ~~~~ !");
        else
            System.out.println(" ! ~~~~ NOT Over ~~~~ !");

        System.err.println("0 : Send token " + token.getValue() + " " + token.getColor() + " step - " + token.steps.get());
        */
    }
}