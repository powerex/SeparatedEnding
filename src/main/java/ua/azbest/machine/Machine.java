package ua.azbest.machine;

import ua.azbest.comunication.Color;
import ua.azbest.comunication.Message;
import ua.azbest.comunication.Token;
import ua.azbest.model.*;

import java.util.concurrent.atomic.AtomicInteger;

public class Machine implements MachineState, Runnable {

    private static int ID = 0;
    private MachineState currentState;
    protected Model cluster;
    private final int id;

    protected Color color;
    protected Token token;
    protected AtomicInteger baseCounter;

    private final double partToSend = 0.7;
    private final double helpChance = 0.08;
    private final double power;

    private long workingTime;
    private long startTimeWorking;
    private int countOn;

    private double taskSize;


    public Machine(double power, Model cluster) {
        this.cluster = cluster;
        this.power = power;
        this.currentState = new PassiveMachine(this);
        id = ID++;
        taskSize = 0.0;
        countOn = 0;
        workingTime = 0;
        baseCounter = new AtomicInteger(0);
        color = Color.WHITE;
    }

    public void setStartTimeWorking(long startTimeWorking) {
        this.startTimeWorking = startTimeWorking;
    }

    public double getTaskSize() {
        return taskSize;
    }

    public void setCurrentState(MachineState currentState) {
        this.currentState = currentState;
    }

    public MachineState getCurrentState() {
        return currentState;
    }

    @Override
    public void receiveMessage(Message message) {
        color = Color.BLACK;
        currentState.receiveMessage(message);
    }

    @Override
    public void sendMessage(Message message) {
        try {
            currentState.sendMessage(generateMessage());
        } catch (UnreachableStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setTaskSize(double taskSize) {
        this.taskSize = taskSize;
    }

    public int getId() {
        return id;
    }

    public Model getCluster() {
        return cluster;
    }

    private Message generateMessage() {
        double size = this.taskSize * partToSend;
        taskSize -= size;
        return new Message(id, size);
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", power=" + power +
                ", taskSize=" + taskSize +
                ", token=" + token +
                '}';
    }

    @Override
    public void run() {
        while (taskSize > 0) {
            taskSize -= power;
            if (Math.random() < helpChance) {
                try {
                    currentState.sendMessage(generateMessage());
                } catch (UnreachableStateException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (taskSize <= 0) {
            setCurrentState(new PassiveMachine(this));
            //System.out.println("Machine " + id + "DONE");
            if (this instanceof ZeroMachine) {
                if (((ZeroMachine) this).isOver()) {
                    cluster.setActive(false);
                }
                cluster.setTokenSend(false);
            }

            if (token != null)
                sendToken();

        }
        workingTime += (System.currentTimeMillis() - startTimeWorking);
    }

    public void increaseCountOn() {
        countOn++;
    }

    public long getWorkingTime() {
        return workingTime;
    }

    public String getStatisticInfo() {
        return "ID: " + String.format("%02d", id) +
                ";\tВключень: " + String.format("%02d", countOn) +
                ";\t Час роботи: " + String.format("%06d", workingTime) +
                ";\t" + String.format("%02.2f", (double) workingTime / getCluster().getWorkTime() * 100) + "%";
    }

    public Statistic getStatistic() {
        return new Statistic(id, countOn, workingTime, String.format("%2.2f",(double) workingTime / getCluster().getWorkTime() * 100)+"%");
    }

    public synchronized void increaseBaseCounter() {
        baseCounter.incrementAndGet();
    }

    public synchronized void decreaseBaseCounter() {
        baseCounter.decrementAndGet();
    }

    public synchronized int getBaseCounter() {
        return baseCounter.get();
    }

    @Override
    public synchronized void sendToken() {
        color = Color.WHITE;
        if (token != null) {
            token.steps.incrementAndGet();
            try {
                currentState.sendToken();
            } catch (UnreachableStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void receiveToken(Token token) {
        this.token = token;
        currentState.receiveToken(token);
        //System.err.println(id + " : Receive token " + " id:" + token.getId() + " value: " + token.getValue() + " " + token.getColor() + " step - " + token.steps.get());
        getCluster().noticeTokenSend(id);
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
